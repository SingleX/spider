package net.singlex.base;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.asynchttpclient.DefaultAsyncHttpClient;

public class BasicConfig {
    protected final DefaultAsyncHttpClient httpClient = new DefaultAsyncHttpClient();
    protected final Config                 config     = ConfigFactory.load();
}
