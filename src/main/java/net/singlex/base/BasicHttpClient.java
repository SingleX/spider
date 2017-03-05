package net.singlex.base;

import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.asynchttpclient.cookie.Cookie;
import org.asynchttpclient.util.HttpConstants;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BasicHttpClient extends BasicConfig {

    protected static List<Cookie> cookies;

    protected void doLogin(String url, String name, String password)
            throws InterruptedException, ExecutionException, TimeoutException {
        final ListenableFuture<Response> futureResponse = httpClient.preparePost(url)
                                                                    .addFormParam("log", name)
                                                                    .addFormParam("pwd", password)
                                                                    .execute();
        final Response response = futureResponse.get(10, TimeUnit.SECONDS);
        if (HttpConstants.ResponseStatusCodes.FOUND_302 == response.getStatusCode()) {
            String location = response.getHeaders().get("Location");
            System.out.println(String.format("302FOUND, location = %s", location));
        }
        cookies = response.getCookies();
    }

    protected Response getWithCookie(String uri)
            throws InterruptedException, java.util.concurrent.ExecutionException,
                   java.util.concurrent.TimeoutException {
        final ListenableFuture<Response> futureResponse = httpClient.prepareGet(uri)
                                                                    .setCookies(cookies)
                                                                    .execute();
        final Response response = futureResponse.get(10, TimeUnit.SECONDS);

        if (response != null && HttpConstants.ResponseStatusCodes.OK_200 != response.getStatusCode()) {
            System.out.println(String.format("URL = %s, StatusCode = %s", response.getUri(), response.getStatusCode()));
        }
        return response;
    }
}
