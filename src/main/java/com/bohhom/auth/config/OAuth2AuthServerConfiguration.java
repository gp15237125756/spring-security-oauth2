package com.bohhom.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    // 该对象将为刷新token提供支持
    @Autowired
    UserDetailsService userDetailsService;

    @Value("${user.oauth.clientId}")
    private String clientID;

    @Value("${user.oauth.clientSecret}")
    private String clientSecret;

    @Value("${user.oauth.redirectUris}")
    private String redirectURLs;

    @Value("${user.oauth.accessTokenValidity}")
    private int accessTokenValidity;

    @Value("${user.oauth.refreshTokenValidity}")
    private int refreshTokenValidity;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();;
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient("password")
                .secret(passwordEncoder.encode(clientSecret))
               // .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorizedGrantTypes("password","refresh_token")
                    .resourceIds("rid")
                .scopes("user_info")
                .authorities("READ_ONLY_CLIENT")
                .redirectUris(redirectURLs)
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory)) //配置令牌的存储（这里存放在内存中）
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

}
