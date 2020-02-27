import core.GraphQLClient

class DefaultGraphQLClient(
    url: String,
    webClient: DefaultGraphQLWebClient,
    jsonConverter: DefaultGraphQLJsonConverter
) :
    GraphQLClient(url, webClient, jsonConverter) {
}