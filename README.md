A simple REST API service using Spring Boot 2 with Spring Security and Spring oAuth2
to demonstrate the issue with the signing key used in the PingFederate access token.

This fails to work with PingFederate versio 8.0 to 8.6, not sure about later 
versions.

It works out of the box with Okta, though.

If you're behind a firewall add the proxy configuration to your run config:

-Dhttp.proxyHost=yourhost -Dhttp.proxyPort=yourproxyport
-Dhttp.nonProxyHosts=”localhost|*.company.com”
-Dhttps.proxyHost=proxy.company.com -Dhttps.proxyPort=yourproxyport
-Dhttps.nonProxyHosts=”localhost|*.company.com”

