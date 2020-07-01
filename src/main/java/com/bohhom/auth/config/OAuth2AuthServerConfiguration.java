package com.bohhom.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    /*@Autowired
    RedisConnectionFactory redisConnectionFactory;*/

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
                .checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
        ;
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                BaseClientDetails details = new BaseClientDetails();
                if ("app".equals(clientId)) {
                    details.setAuthorizedGrantTypes(Arrays.asList("password", "refresh_token"));
                } else if ("web".equals(clientId)) {
                    details.setAuthorizedGrantTypes(Arrays.asList("client_credentials", "refresh_token"));
                } else {
                    throw new NoSuchClientException("客户端id错误");
                }
                details.setClientId(clientId);
                details.setClientSecret(passwordEncoder.encode(clientSecret));
                details.setScope(Arrays.asList("user_info"));
                details.setAccessTokenValiditySeconds(accessTokenValidity);
                details.setRefreshTokenValiditySeconds(refreshTokenValidity);
                details.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_APP"));
                return details;
            }
        });
                   /* .inMemory()
                    .withClient("password")
                .secret(passwordEncoder.encode(clientSecret))
               // .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorizedGrantTypes("password","refresh_token")
                    .resourceIds("rid")
                .scopes("user_info")
                .authorities("READ_ONLY_CLIENT")
                .redirectUris(redirectURLs)
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity);*/
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore()) //配置令牌的存储（这里存放在内存中）
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .accessTokenConverter(accessTokenConverter());
    }

}
