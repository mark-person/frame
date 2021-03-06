/**
 * 
 */
package com.ppx.cloud.example.graphql.test;



import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.RuntimeWiring.Builder;
/**
 * @author mark
 * @date 2020年1月15日
 */
public class GraphQLTes_bak0 {
	
	
	static DataFetcher userDataFetcher = new DataFetcher() {
        @Override
        public String get(DataFetchingEnvironment environment) {
        	
//        	environment.getArgument("")
        	
            return "myString 123";
        }
    };

	
	public static void main(String[] args) {
		// Scalar在graphql.schema.idl.ScalarInfo
		
		// Int:Integer Float:Double String:String Boolean:Boolean ID:String Short:Short
		// Byte:Byte BigInteger:BigInteger BigDecimal:BigDecimal Char:Character
		
		// String schema = "schema{query: Query} type Query{hello:String \n id:Int}";
		
		
		String schema = "schema{query: MyPojo \n mutation: MyMutation} type MyPojo{id(id:ID): Int \n name: String} type MyMutation{setSomeField(to: String): String}";
		

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

//        RuntimeWiring runtimeWiring = newRuntimeWiring()
//                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
//                .build();
        Map<String, Object> map = Map.of("name", "deng");
        
        Builder builder = newRuntimeWiring();
        
        builder.type("MyPojo", b -> b.dataFetcher("id",  new StaticDataFetcher("123456")));
        builder.type("MyPojo", b -> b.dataFetcher("name",  userDataFetcher));
        
        RuntimeWiring runtimeWiring = builder.build();
        

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{id(id:123)}");
        
        Map<String, Object> resultMap = executionResult.toSpecification();
        
        
        try {
        	System.out.println(new ObjectMapper().writeValueAsString(resultMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Prints: {hello=world}
	}
}
