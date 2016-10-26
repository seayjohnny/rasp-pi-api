package raspipi.api;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.Map;

public class GenericRequest {

    private static String Url;
    private static String Method;
    private static Map<String, String> Params;
    private static ResponseObject Response;

    static final HttpTransport  HTTP_TRANSPORT = new NetHttpTransport();
    static final HttpRequestFactory REQUEST_FACTORY = HTTP_TRANSPORT.createRequestFactory();

    public GenericRequest() {
    }

    public GenericRequest(String url, String method) {
        this.setUrl(url);
        this.setMethod(method);
        this.setParams(null);

        HttpContent content = new UrlEncodedContent(this.getParams());

        try {
            HttpRequest request = REQUEST_FACTORY.buildGetRequest(new GenericUrl(this.getUrl()));
            this.setResponse(new ResponseObject(request.execute()));
        } catch (IOException e){
            System.err.println(e.getMessage().toString());
        }
    }

    public GenericRequest(String url, String method, Map<String, String> params) {
        this.setUrl(url);
        this.setMethod(method);
        this.setParams(params);

        HttpContent content = new UrlEncodedContent(this.getParams());

        try {
            HttpRequest request = REQUEST_FACTORY.buildPostRequest(new GenericUrl(this.getUrl()), content);
            this.setResponse(new ResponseObject(request.execute()));
        } catch (IOException e){
            System.err.println(e.getMessage().toString());
        }
    }

    public static String getUrl() {
        return Url;
    }

    public static void setUrl(String url) {
        Url = url;
    }

    public static String getMethod() {
        return Method;
    }

    public static void setMethod(String method) {
        Method = method;
    }

    public static Map<String, String> getParams() {
        return Params;
    }

    public static void setParams(Map<String, String> params) {
        Params = params;
    }

    public static ResponseObject getResponse() {
        return Response;
    }

    private static void setResponse(ResponseObject response){
        Response = response;
    }

}
