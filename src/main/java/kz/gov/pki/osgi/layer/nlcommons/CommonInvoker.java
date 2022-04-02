package kz.gov.pki.osgi.layer.nlcommons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;
import kz.gov.pki.osgi.layer.annotations.NCALayerClass;
import kz.gov.pki.osgi.layer.annotations.NCALayerMethod;
import kz.gov.pki.osgi.layer.exception.CommonException;
import kz.gov.pki.osgi.layer.exception.CommonFailure;
import kz.gov.pki.osgi.layer.nlcommons.data.CommonResponse;
import lombok.NonNull;

/**
 * 
 * @author aslan
 *
 */

public final class CommonInvoker {

	private static final String COMMON_JSON_WRAPPING_EXCEPTION_RESPONSE =
			"{\"success\": false, \"code\": \"COMMON_JSON_WRAPPING_EXCEPTION\"}";
	
	private List<Object> nlClasses;

	public CommonInvoker(@NonNull List<Object> nlClasses) {

		for (Object nlClass : nlClasses) {
			if (!nlClass.getClass().isAnnotationPresent(NCALayerClass.class)) {
				throw new IllegalArgumentException(nlClass.getClass().getName() +
						" is not annotated as NCALayerClass");
			}
		}
		this.nlClasses = Collections.unmodifiableList(nlClasses);

	}
	
	public CommonInvoker(@NonNull Object nlClass) {

		this(Arrays.asList(nlClass));

	}

	public String invoke(@NonNull String jsonString) {
	    return invoke(jsonString, "{}");
	}
	
	public String invoke(@NonNull String jsonString, @NonNull String jsonAddInfo) {

		CommonResponse<Object> response;
		JSONObject obj = new JSONObject(jsonString);
		int jsonApiVersion = obj.optInt("apiVersion", 2);
		String jsonClazz = obj.optString("class");
		String jsonMethod = obj.getString("method");
		JSONObject jsonArgs = obj.optJSONObject("args");

		try {
			if (jsonApiVersion != 2) {
				throw new IllegalArgumentException("Supported apiVersion is 2");
			}
			
			Object nlClassObj = null;
			if (jsonClazz.isEmpty()) {
				nlClassObj = nlClasses.get(0);
			} else {
				if (nlClasses.size() == 1) {
					if (!jsonClazz.equals(nlClasses.get(0).getClass().getSimpleName())) {
						throw new IllegalArgumentException(jsonClazz + " is not found");
					}
					nlClassObj = nlClasses.get(0);
				} else {
					nlClassObj = nlClasses.stream().filter(c ->
						c.getClass().getSimpleName().equals(jsonClazz)).
						findFirst().orElseThrow(() ->
							new IllegalArgumentException(jsonClazz + " is not found"));
				}
			}

			Class<?> nlClass = nlClassObj.getClass();
			Method[] methodArr = nlClass.getMethods();
			List<Method> methods = Arrays.asList(methodArr).stream().
					filter(e -> e.isAnnotationPresent(NCALayerMethod.class)).
					collect(Collectors.toList());

			Method method = methods.stream().
					filter(e -> jsonMethod.equals(
							e.getDeclaredAnnotation(NCALayerMethod.class).name()) || 
							jsonMethod.equals(e.getName())).
					findFirst().
					orElseThrow(() -> new IllegalArgumentException(jsonMethod + 
								" is not defined in " + nlClass.getSimpleName()));

			Object result;
			if (method.getParameterCount() == 0) {
				result = method.invoke(nlClassObj);
			} else {
				if (jsonArgs == null) {
					throw new IllegalArgumentException(jsonMethod + " demands arguments");
				}
				result = method.invoke(nlClassObj, jsonArgs.toString(), jsonAddInfo);
			}
			
			response = CommonResponse.builder().status(true).build();
			response.setBody(result);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			response = buildErrorResponse(e);
		}
		
		String ret = (JSONObject.wrap(response)).toString();
		
		if (ret == null) {
			ret = COMMON_JSON_WRAPPING_EXCEPTION_RESPONSE;
		}
		
		return ret;
	}

	private CommonResponse<Object> buildErrorResponse(@NonNull Exception e) {
		
		CommonResponse<Object> response = CommonResponse.builder().build();
		if (e instanceof CommonException) {
		    CommonException ce = (CommonException) e;
		    response.setFailure(ce.getFailure());
		    Throwable cause = ce.getCause();
            if (cause != null) {
                response.setDetails(cause.toString());
            }
		} else if (e instanceof InvocationTargetException) {
			if (e.getCause() instanceof CommonException) {
			    CommonException ce = (CommonException) e.getCause();
		        response.setFailure(ce.getFailure());
		        Throwable cause = ce.getCause();
		        if (cause != null) {
		            response.setDetails(cause.toString());
		        }
			} else {
				response.setFailure(CommonFailure.INVOCATION_ERROR);
				if (e.getCause() != null) {
					response.setDetails(e.getCause().toString());
				} else {
					response.setDetails(e.toString());
				}
			}
		} else {
			response.setFailure(CommonFailure.GENERAL_ERROR);
			response.setDetails(e.toString());
		}

		return response;
	}
	
	public String buildJSONErrorResponse(@NonNull Exception e) {
		return JSONObject.wrap(buildErrorResponse(e)).toString();
	}
	
}
