package services

import androidx.compose.foundation.pager.PageSize
import kotlinx.serialization.Serializable

enum class LogicalOperatorEnum(val value: Int) {
    AND(10),
    OR(20),
    NOT(30)
}

enum class ComparisonOperatorEnum(val value: Int) {
    EQUAL(10),
    NOT_EQUAL(20),
    GREATER_THAN(30),
    GREATER_THAN_OR_EQUAL(40),
    LESS_THAN(50),
    LESS_THAN_OR_EQUAL(60),
    LIKE(70),
//    NOT_LIKE(80),
    IN(90),
//    NOT_IN(100),
    //BETWEEN = 110,
    //NOT_BETWEEN = 120,
    //IS_NULL = 130,
    //IS_NOT_NULL = 140
}


enum class OrderOperator(val value: Int) {
    ASC(10),
    DESC(20),
    THEN_ASC(30),
    THEN_DESC(40)
}

@Serializable
data class Order(
    val operator: OrderOperator,
    val property: String
)

@Serializable
data class FilterExpression(
    val comparisonOperator: ComparisonOperatorEnum,
    val property: String,
    val value: String
)

@Serializable
data class Filter(
    val logicalOperator: LogicalOperatorEnum,

    val expressions: List<FilterExpression>?
)

@Serializable
data class GetRequest(
    val filter: Filter?,
    val order: List<OrderOperator>?,
    val page: Int?,
    val pageSize: Int?
)

fun getDefaultFilter(): Filter {
    return Filter(
        LogicalOperatorEnum.AND,
null
    )
}