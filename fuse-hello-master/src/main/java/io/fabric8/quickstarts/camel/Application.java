/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.quickstarts.camel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;


/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends SpringBootServletInitializer {

	Logger logger = LoggerFactory.getLogger(Application.class);
	
    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
            restConfiguration()
                .contextPath("")
                    .apiProperty("api.title", "Camel REST API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiProperty("api.specification.contentType.json", "application/vnd.oai.openapi+json;version=3.0")
                    .apiProperty("api.specification.contentType.yaml", "application/vnd.oai.openapi;version=3.0")
                    .apiContextRouteId("doc-api")
                .component("jetty")
                .port(8080)
                .bindingMode(RestBindingMode.off);

            rest("/hello/get").description("REST service")
                .get().description("Hello World")
                    .route().routeId("hello-world")
                    .process(new ProcessorFuse())
                    .setBody().constant("{ \"mensaje\" : \"Hello World\" }")   
                    .log(">>> ${body}") 
                    .setHeader("Content-Type").constant("application/json");
            
        }
        
    }
    
    private class ProcessorFuse implements Processor {

		@Override
		public void process(Exchange ex) throws Exception {
			
			logger.info("Ingresando al Procesor");	
			int numero=0;
			int numero2=0;
			List<Integer> lista = new ArrayList<Integer>();
			for(int i=1;i<=1000;i++){ 
			   numero = (int) (Math.random()* 10000000) + 6000000;      
			   lista.add(numero);
			}
			Iterator<Integer> iter = lista.iterator();
			while (iter.hasNext()) {
				numero2=iter.next();
			}
//			Thread.sleep(500);
			logger.info("Numero Obtenido: "+numero2);	
		}
	}
    
}
