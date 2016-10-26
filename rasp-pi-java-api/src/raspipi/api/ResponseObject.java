package raspipi.api;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResponseObject {
    private static Integer Status;
    private static Map<String, String> Header;
    private static Map<String, Object> Body;

    public ResponseObject() {
        this.setStatus(0);
        this.setHeader(null);
        this.setBody(null);
    }

    public ResponseObject(Integer status){
        this.setStatus(status);
        this.setHeader(null);
        this.setBody(null);
    }

    public ResponseObject(Integer status, Map<String, String> header){
        this.setStatus(status);
        this.setHeader(header);
        this.setBody(null);
    }

    public ResponseObject(Integer status, Map<String, String> header, Map<String, Object> body){
        this.setStatus(status);
        this.setHeader(header);
        this.setBody(body);
    }

    public ResponseObject(HttpResponse response){
        this.setStatus(response.getStatusCode());

        HttpHeaders httpHeaders = response.getHeaders();
        Iterator iterator = httpHeaders.keySet().iterator();

        Map<String, String> header = new HashMap<>();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            header.put(key, httpHeaders.getHeaderStringValues(key).toString());
        }
        this.setHeader(header);
        try{
            this.setBody(convertStreamToMap(response.getContent()));
        } catch (IOException e){

        }


    }

    public static Integer getStatus() {
        return Status;
    }

    public static void setStatus(Integer status) {
        Status = status;
    }

    public static Map<String, String> getHeader() {
        return Header;
    }

    public static void setHeader(Map<String, String> header) {
        Header = header;
    }

    public static Map<String, Object> getBody() {
        return Body;
    }

    public static void setBody(Map<String, Object> body) {
        Body = body;
    }

    private static Map<String, Object> convertStreamToMap(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String string = s.hasNext() ? s.next() : "";
        Map<String, Object> map = new HashMap<>();
        string = string.substring(2, string.lastIndexOf('}'));
        String[] pairs = string.split(",");
        for(int i=0;i<pairs.length;i++){
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            String key = keyValue[0].substring(1, keyValue[0].lastIndexOf('"'));
            map.put(key, keyValue[1]);
        }
        return map;
    }
}
