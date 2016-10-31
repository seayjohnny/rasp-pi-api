package rasppi.api.requests;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import rasppi.api.objects.ResponseObject;
import rasppi.api.exceptions.InvalidRequestMethod;
import rasppi.api.exceptions.MissingParams;

import java.io.IOException;
import java.util.Map;

public class GenericRequest {

    private String Url;
    private String Method;
    private Map<String, Object> Params;
    private ResponseObject Response;

    static final HttpTransport  HTTP_TRANSPORT = new NetHttpTransport();
    static final HttpRequestFactory REQUEST_FACTORY = HTTP_TRANSPORT.createRequestFactory();

    public GenericRequest() {
    }

    public GenericRequest(String url, String method) {
        this.setUrl(url);
        this.setMethod(method.toLowerCase());
        this.setParams(null);
    }

    public GenericRequest(String url, String method, Map<String, Object> params) {
        this.setUrl(url);
        this.setMethod(method);
        this.setParams(params);
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public Map<String, Object> getParams() {
        return Params;
    }

    public void setParams(Map<String, Object> params) {
        Params = params;
    }

    public ResponseObject getResponse() {
        return Response;
    }

    public void execute(){

        try {
            if(this.Method == "get") {
                HttpRequest request = REQUEST_FACTORY.buildGetRequest(new GenericUrl(this.getUrl()));
                this.setResponse(new ResponseObject(request.execute()));

            } else if(this.Method == "post"){
                if(!this.Params.isEmpty()){
                    HttpContent content = new UrlEncodedContent(this.getParams());
                    HttpRequest request = REQUEST_FACTORY.buildPostRequest(new GenericUrl(this.getUrl()), content);
                    this.setResponse(new ResponseObject(request.execute()));

                } else {
                    throw new MissingParams("Missing parameters for POST request.");
                }

            } else {
                throw new InvalidRequestMethod();

            }
        }
        catch (IOException e){ System.err.println(e.getMessage().toString());}
        catch (MissingParams e){ System.err.println(e.getMessage().toString());}
        catch (InvalidRequestMethod e) { System.err.println(e.getMessage().toString());}
    }

    private void setResponse(ResponseObject response){
        Response = response;
    }
}
