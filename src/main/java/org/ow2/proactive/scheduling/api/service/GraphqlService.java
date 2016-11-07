/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.ow2.proactive.scheduling.api.service;

import static graphql.schema.GraphQLSchema.newSchema;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ow2.proactive.scheduling.api.controller.GraphQLController;
import org.ow2.proactive.scheduling.api.schema.type.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.annotations.GraphQLAnnotations;
import graphql.schema.GraphQLObjectType;


/**
 * @author ActiveEon Team
 */
@Service
public class GraphqlService {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

    private GraphQLObjectType queryObject = GraphQLAnnotations.object(Query.class);

    private GraphQL graphql = new GraphQL(newSchema().query(queryObject).build());

    public GraphqlService() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
    }

    public Map<String, Object> executeQuery(String query, String operationName, Map<String, Object> variables) {
        // TODO see how to integrate data fetcher instead of
        // static value (cf. second parameter passed to execute)

        ExecutionResult executionResult = graphql.execute(query, operationName, null, variables);

        Map<String, Object> result = new LinkedHashMap<>();

        if (!executionResult.getErrors().isEmpty()) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }

        result.put("data", executionResult.getData());

        return result;

    }

}