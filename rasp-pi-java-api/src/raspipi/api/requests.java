package raspipi.api;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class requests {
    private static final String PI_URL = "http://192.168.0.2/";

    private static final String CREATE_URL = "db_create.php";
    private static final String UPDATE_URL = "db_update.php";
    private static final String REMOVE_URL = "db_remove.php";
    private static final String VIEW_URL = "db_view.php";

    static final HttpTransport  HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    static final HttpRequestFactory REQUEST_FACTORY = HTTP_TRANSPORT.createRequestFactory();

    public static Map<String, String> VariableParameters(String id, String name, String type, String val){
        Map<String, String> params = new HashMap<>(4);
        params.put("id", id);
        params.put("name", name);
        params.put("type", type);
        params.put("val", val);

        return params;
    }

    public static class VariableRequest {

        GenericUrl url;
        String id;
        String name;
        String type;
        String val;
        String response;

        public VariableRequest(){
        }

        public VariableRequest(GenericUrl url, String id, String name, String type, String val) {
            this.setUrl(url);
            this.setId(id);
            this.setName(name);
            this.setType(type);
            this.setVal(val);



            Map<String, String> params = VariableParameters(this.getId(), this.getName(), this.getType(), this.getVal());
            HttpContent content = new UrlEncodedContent(params);

            try {
                HttpRequest request = REQUEST_FACTORY.buildPostRequest(this.getUrl(), content);
                request.setContent(content);
            } catch (IOException e){
                System.err.println(e.getMessage().toString());
            }

        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public GenericUrl getUrl() {
            return url;
        }

        public void setUrl(GenericUrl url) {
            this.url = url;
        }



    }
    public static class CreateVariableRequest extends VariableRequest{
        public CreateVariableRequest(GenericUrl url, String id, String name, String type, String val) {
            super(url, id, name, type, val);
        }
    }
    public static class UpdateVariableRequest extends VariableRequest{
        public UpdateVariableRequest(GenericUrl url, String id, String name, String type, String val) {
            super(url, id, name, type, val);
        }
    }
    public static class RemoveVariableRequest extends VariableRequest{
        public RemoveVariableRequest(GenericUrl url, String id) {
            this.setUrl(url);
            this.setId(id);

            Map<String, String> params = new HashMap<>(1);
            params.put("id", this.getId());

            HttpContent content = new UrlEncodedContent(params);

            try {
                HttpRequest request = REQUEST_FACTORY.buildPostRequest(this.getUrl(), content);
                request.setContent(content);
                this.setResponse(request.execute().parseAsString());
            } catch (IOException e){
                System.err.println(e.getMessage().toString());
            }
        }
    }
    public static class ViewVariableRequest extends VariableRequest{
        Map<String, Object> vars;

        public ViewVariableRequest(GenericUrl url) {
            this.setUrl(url);

            try {
                HttpRequest request = REQUEST_FACTORY.buildPostRequest(this.getUrl(), null);
                request.setContent(null);
                this.setResponse(request.execute().parseAsString());
                System.out.println(response);

            } catch (IOException e){
                System.err.println(e.getMessage().toString());
            }
        }

        public Map<String, Object> getVars() {
            return vars;
        }

        public void setVars(Map<String, Object> vars) {
            this.vars = vars;
        }


    }

    public static void create(String id, String name, String type, String value) throws IOException{

        GenericUrl url = new GenericUrl(PI_URL.concat(CREATE_URL));
        VariableRequest request = new CreateVariableRequest(url, id, name, type, value);
    }

    public static void update(String id, String name, String type, String value) throws IOException{

        GenericUrl url = new GenericUrl(PI_URL.concat(UPDATE_URL));
        VariableRequest request = new UpdateVariableRequest(url, id, name, type, value);
    }

    public static void remove(String id) throws IOException{

        GenericUrl url = new GenericUrl(PI_URL.concat(REMOVE_URL));
        VariableRequest request = new RemoveVariableRequest(url, id);
    }

    public static void view() throws IOException{

        GenericUrl url = new GenericUrl(PI_URL.concat(VIEW_URL));
        VariableRequest request = new ViewVariableRequest(url);
    }

    public static void main(String[] args){
        String url = "http://192.168.0.2/db_create.php";
        String method = "POST";
        Map<String, String> params = new HashMap<>(4);
        params.put("id", "6");
        params.put("name", "var6");
        params.put("type", "string");
        params.put("val", "erwqer");

        GenericRequest gr = new GenericRequest(url, method, params);
        ResponseObject ro = gr.getResponse();
        System.out.println(ro.getStatus());
    }

}
