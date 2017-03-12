package net.singlex.base;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.asynchttpclient.BoundRequestBuilder;
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

    protected void httpPostForLogin(String url, List<Pair<String, String>> formParams)
            throws InterruptedException, ExecutionException, TimeoutException {
        BoundRequestBuilder builder = httpClient.preparePost(url);
        if (CollectionUtils.isNotEmpty(formParams)) {
            formParams.forEach(p -> builder.addFormParam(p.getLeft(), p.getRight()));
        }
        final ListenableFuture<Response> futureResponse = builder.execute();

        final Response response = futureResponse.get(10, TimeUnit.SECONDS);
        if (HttpConstants.ResponseStatusCodes.FOUND_302 == response.getStatusCode()) {
            String location = response.getHeaders().get("Location");
            System.out.println(String.format("302FOUND, location = %s", location));
        }
        cookies = response.getCookies();
    }

    protected Response httpGetWithCookie(String uri)
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
