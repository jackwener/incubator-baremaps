/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.baremaps.server;



import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;

/**
 * A filter that sets the base URI of the request.
 */
@Provider
@PreMatching
@Singleton
public class BaseUriRequestFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    var baseUri = UriBuilder.fromUri(requestContext.getUriInfo().getBaseUri());
    var headers = requestContext.getHeaders();
    if (headers.get("X-Forwarded-Proto") != null) {
      baseUri.scheme(headers.getFirst("X-Forwarded-Proto"));
    }
    if (headers.get("X-Forwarded-Host") != null) {
      baseUri.host(headers.getFirst("X-Forwarded-Host"));
    }
    if (headers.get("X-Forwarded-Port") != null) {
      baseUri.port(Integer.parseInt(headers.getFirst("X-Forwarded-Port")));
    }
    requestContext.setRequestUri(baseUri.build(), requestContext.getUriInfo().getRequestUri());
  }
}
