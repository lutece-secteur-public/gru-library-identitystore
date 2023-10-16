![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=gru-library-identitystore-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identitystore&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identitystore)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identitystore&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identitystore)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identitystore&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identitystore)

# Librairie Identitystore

## Introduction

Cette librairie fournit des services pour communiquer avec l'API REST d'Identity Store.

Pour plus d'information, la documentation de la librairie est disponible sur le [wiki officiel](https://lutece.paris.fr/support/wiki/gru-library-identitystore-v3.html).

## Services

Le service principal est `fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityService` , qui fournit les m&eacute;thodes permettant d'appeler Identity Store.

Ces m&eacute;thodes sont implémentées par le service de transport REST `fr.paris.lutece.plugins.identitystore.v3.web.rs.service.IdentityTransportRest` .

Il requiert une impl&eacute;mentation de `fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider` pour d&eacute;finir le transport HTTP. Deux impl&eacute;mentations de cette interface sont pr&eacute;sentes dans cette librairie :
 
*  `fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpAccessTransport` , qui utilise des requ&ecirc;tes simples
*  `fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpApiManagerAccessTransport` , qui utilise l'ApiManager pour s&eacute;curiser les requ&ecirc;tes&agrave; l'API (en utilisant des tokens)


Ces deux impl&eacute;mentations ont besoin de la d&eacute;finition de l'URL vers le service d'Identity Store.
 
*  `apiEndPointUrl` , l'URL de l'endpoint de l'Identity store.


Pour utiliser `HttpApiManagerAccessTransport` , deux paramètres supplémentaires sont requis:
 
*  `accessManagerEndPointUrl` , l'URL de l'endpoint de l'API manager pour récupérer le token.
*  `accessManagerCredentials` , les credentials permettant d'accéder à l'API manager pour récupérer le token.


## Configuration en utilisant le contexte Spring

Premi&egrave;rement, d&eacute;finir le bean pour le transport HTTP&agrave; utiliser :
 
* renseigner la propri&eacute;t&eacute; pour l'URL pointant vers le service d'Identity Store
* renseigner les autres propri&eacute;t&eacute;s si le transport HTTP `HttpApiManagerAccessTransport` est utilis&eacute;


Ensuite, d&eacute;finir le bean pour le transport REST `IdentityTransportRest` :
 
* comme argument de constructeur, faire r&eacute;f&eacute;rence au bean pour le transport HTTP


Ensuite, d&eacute;finir le bean pour le service `IdentityService` :
 
* comme argument de constructeur, faire r&eacute;f&eacute;rence au bean pour le transport REST


Voici un exemple de configuration Spring avec le transport HTTP `HttpAccessTransport` :
```

<!-- library identitystore -->
<!-- IHttpTransportProvider declarations -->
<bean id="identitystore.httpAccessTransport" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpAccessTransport" >
    <property name="apiEndPointUrl">
        <value>${identitydesk.identitystore.apiEndPointUrl}</value>
    </property>
</bean>

<bean id="identity.restTransport.httpAccess" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.IdentityTransportRest">
    <constructor-arg ref="identitystore.httpAccessTransport"/>
</bean>

<!-- IdentityService impl -->
<bean id="identity.identityService" class="fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityService">
    <constructor-arg ref="identity.restTransport.httpAccess"/>
</bean>
                        
```


Voici un exemple de configuration Spring avec le transport HTTP `HttpApiManagerAccessTransport` :
```

<!-- library identitystore -->
<!-- IHttpTransportProvider declarations -->
<bean id="identitystore.httpAccessTransport" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.HttpApiManagerAccessTransport">
    <property name="apiEndPointUrl">
        <value>${myplugin.identitystore.ApiEndPointUrl}</value>
    </property>
    <property name="accessManagerEndPointUrl">
        <value>${myplugin.identitystore.accessManagerEndPointUrl}</value>
    </property>
    <property name="accessManagerCredentials">
        <value>${myplugin.identitystore.accessManagerCredentials}</value>
    </property>
</bean>

<bean id="identity.restTransport.httpAccess" class="fr.paris.lutece.plugins.identitystore.v3.web.rs.service.IdentityTransportRest">
    <constructor-arg ref="identitystore.httpAccessTransport"/>
</bean>

<!-- IdentityService impl -->
<bean id="identity.identityService" class="fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityService">
    <constructor-arg ref="identity.restTransport.httpAccess"/>
</bean>
                        
```


Vous pouvez maintenant accéder au bean comme suit:
```

                            import fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityService;
                            ...
                            private IdentityService _identityService = SpringContextService.getBean( "identity.identityService" );
                        
```


## Configuration dans le code Java

Le service peut&ecirc;tre directement cr&eacute;&eacute; dans le code Java. Voici un exemple avec le transport HTTP `HttpApiManagerAccessTransport` (le m&ecirc;me m&eacute;canisme peut&ecirc;tre appliqu&eacute; pour le transport HTTP `HttpAccessTransport` ).

Premi&egrave;rement, d&eacute;finir les cl&eacute;s suivantes dans un fichier de propri&eacute;t&eacute;s :
```

myplugin.identitystore.ApiEndPointUrl=http://mydomain.com/url/to/apimanager/api/identitystore
myplugin.identitystore.accessManagerEndPointUrl=http://mydomain.com/url/to/apimanager/token
myplugin.identitystore.accessManagerCredentials=your_private_key
                        
```


Ensuite, ajouter le code suivant dans le code Java :
```

...
private static final String PROPERTY_GRU_API_ENDPOINT = "myplugin.identitystore.ApiEndPointUrl";
private static final String PROPERTY_GRU_AM_ENDPOINT = "myplugin.identitystore.accessManagerEndPointUrl";
private static final String PROPERTY_GRU_AM_CREDENTIALS = "myplugin.identitystore.accessManagerCredentials";
...

HttpApiManagerAccessTransport httpApiManagerAccessTransport = new HttpApiManagerAccessTransport( )

httpApiManagerAccessTransport.setApiEndPointUrl( AppPropertiesService.getProperty( PROPERTY_GRU_API_ENDPOINT ) );
httpApiManagerAccessTransport.setAccessManagerEndPointUrl( AppPropertiesService.getProperty( PROPERTY_GRU_AM_ENDPOINT ) );
httpApiManagerAccessTransport.setAccessManagerCredentials( AppPropertiesService.getProperty( PROPERTY_GRU_AM_CREDENTIALS ) );

IdentityTransportRest  identityTransport = new IdentityTransportRest( httpApiManagerAccessTransport );

final IdentityService identityService = new IdentityService( identityTransport );
...
                        
```



[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/library-identitystore/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*