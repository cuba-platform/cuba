
grammar JPA;

// Alexander Kunkel
// More JPA informations:
// http://www.kunkelgmbh.de/jpa/jpa.html
// 28.11.2009

// Alexander Chevelev
// 15.10.2010

options{
        backtrack=true;
        output=AST;
}

tokens {
    T_SELECTED_ITEMS;
    T_SELECTED_ITEM;
    T_SOURCES;
    T_SOURCE;
    T_SELECTED_FIELD;
    T_SELECTED_ENTITY;
    T_ID_VAR;
    T_JOIN_VAR;
    T_COLLECTION_MEMBER;
    T_QUERY;
    T_CONDITION;
    T_SIMPLE_CONDITION;
    T_PARAMETER;
    T_GROUP_BY;
    T_ORDER_BY;
    T_ORDER_BY_FIELD;
    T_AGGREGATE_EXPR;
    HAVING = 'HAVING';
    ASC = 'ASC';
    DESC = 'DESC';
    AVG = 'AVG';
    MAX = 'MAX';
    MIN = 'MIN';
    SUM = 'SUM';
    COUNT = 'COUNT';
    OR = 'OR';
    AND = 'AND';
    LPAREN = '(';
    RPAREN = ')';
    DISTINCT = 'DISTINCT';
    LEFT = 'LEFT';
    OUTER = 'OUTER';
    INNER = 'INNER';
    JOIN = 'JOIN';
    FETCH = 'FETCH';
    ORDER = 'ORDER';
    GROUP = 'GROUP';
    BY = 'BY';
}


@header {
package com.haulmont.cuba.core.sys.jpql.antlr;

import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;
import com.haulmont.cuba.core.sys.jpql.tree.SelectedItemNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.FromNode;
import com.haulmont.cuba.core.sys.jpql.tree.SelectionSourceNode;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.CollectionMemberNode;
import com.haulmont.cuba.core.sys.jpql.tree.WhereNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import com.haulmont.cuba.core.sys.jpql.tree.ParameterNode;
import com.haulmont.cuba.core.sys.jpql.tree.GroupByNode;
import com.haulmont.cuba.core.sys.jpql.tree.OrderByNode;
import com.haulmont.cuba.core.sys.jpql.tree.OrderByFieldNode;
import com.haulmont.cuba.core.sys.jpql.tree.AggregateExpressionNode;
}

@lexer::header {
package com.haulmont.cuba.core.sys.jpql.antlr;

}


ql_statement
    : select_statement;

select_statement
    :  sl='SELECT' select_clause from_clause (where_clause)? (groupby_clause)? (having_clause)?(orderby_clause)?
     -> ^(T_QUERY<QueryNode>[$sl] (select_clause)? from_clause (where_clause)? (groupby_clause)? (having_clause)? (orderby_clause)?);

from_clause
    : fr='FROM' identification_variable_declaration (',' identification_variable_or_collection_declaration)*
    -> ^(T_SOURCES<FromNode>[$fr] identification_variable_declaration identification_variable_or_collection_declaration*);

identification_variable_or_collection_declaration
    : identification_variable_declaration
    | collection_member_declaration -> ^(T_SOURCE<SelectionSourceNode> collection_member_declaration);

identification_variable_declaration
    : range_variable_declaration joined_clause*
    -> ^(T_SOURCE<SelectionSourceNode> range_variable_declaration joined_clause*);

joined_clause
    : join | fetch_join;

range_variable_declaration
    : range_variable_declaration_source ('AS')? identification_variable
      -> ^(T_ID_VAR<IdentificationVariableNode>[$identification_variable.text] range_variable_declaration_source )
    ;

range_variable_declaration_source
    : abstract_schema_name
    | lp='(SELECT' select_clause from_clause (where_clause)? (groupby_clause)? (having_clause)?(orderby_clause)? rp=')'
     -> ^(T_QUERY<QueryNode>[$lp, $rp] (select_clause)? from_clause (where_clause)? (groupby_clause)? (having_clause)? (orderby_clause)?)
    ;

join
    : join_spec join_association_path_expression ('AS')? identification_variable
      -> ^(T_JOIN_VAR<JoinVariableNode>[$join_spec.text, $identification_variable.text] join_association_path_expression )
      ;

fetch_join
    : join_spec 'FETCH' join_association_path_expression;

join_spec
    :(('LEFT') ('OUTER')? | 'INNER' )? 'JOIN';

join_association_path_expression
    : identification_variable '.' (field'.')* field?
    -> ^(T_SELECTED_FIELD<PathNode>[$identification_variable.text] (field)*)
    ;

collection_member_declaration
    : 'IN''(' path_expression ')' ('AS')? identification_variable
    -> ^(T_COLLECTION_MEMBER<CollectionMemberNode>[$identification_variable.text] path_expression )
    ;

path_expression
    :  identification_variable '.' (field'.')* (field)?
    -> ^(T_SELECTED_FIELD<PathNode>[$identification_variable.text] (field)*)
    ;

select_clause
    : ('DISTINCT')? select_expression (',' select_expression)*
    -> ^(T_SELECTED_ITEMS ('DISTINCT')? ^(T_SELECTED_ITEM<SelectedItemNode>[] select_expression)*)
    ;

select_expression
    : path_expression
    | aggregate_expression
    | identification_variable -> ^(T_SELECTED_ENTITY<PathNode>[$identification_variable.text])
    | 'OBJECT' '('identification_variable')'
    | constructor_expression;

constructor_expression
    : 'NEW' constructor_name '(' constructor_item (',' constructor_item)* ')';

constructor_item
    : path_expression | aggregate_expression;

aggregate_expression
    : aggregate_expression_function_name '(' ('DISTINCT')? path_expression')'
    -> ^(T_AGGREGATE_EXPR<AggregateExpressionNode>[] aggregate_expression_function_name '(' ('DISTINCT')? path_expression')')
    | 'COUNT' '(' ('DISTINCT')? identification_variable ')'
    -> ^(T_AGGREGATE_EXPR<AggregateExpressionNode>[] 'COUNT' '(' ('DISTINCT')? identification_variable ')');

aggregate_expression_function_name
    : 'AVG' | 'MAX' | 'MIN' |'SUM' | 'COUNT';

where_clause
    : wh='WHERE' conditional_expression-> ^(T_CONDITION<WhereNode>[$wh] conditional_expression)
    | 'WHERE' path_expression -> ^(T_CONDITION<WhereNode>[$wh] path_expression);

groupby_clause
    : 'GROUP' 'BY' groupby_item (',' groupby_item)*
    -> ^(T_GROUP_BY<GroupByNode>[] 'GROUP' 'BY' groupby_item*)
    ;

groupby_item
    : path_expression | identification_variable;

having_clause
    : 'HAVING' conditional_expression;

orderby_clause
    : 'ORDER' 'BY' orderby_item (',' orderby_item)*
    -> ^(T_ORDER_BY<OrderByNode>[] 'ORDER' 'BY' orderby_item*);

orderby_item
    : path_expression   ('ASC')?
     -> ^(T_ORDER_BY_FIELD<OrderByFieldNode>[] path_expression ('ASC')?)
    | path_expression   'DESC'
    -> ^(T_ORDER_BY_FIELD<OrderByFieldNode>[] path_expression 'DESC');

subquery
    : lp='(SELECT' simple_select_clause subquery_from_clause (where_clause)? (groupby_clause)? (having_clause)? rp=')'
     -> ^(T_QUERY<QueryNode>[$lp,$rp] simple_select_clause subquery_from_clause (where_clause)? (groupby_clause)? (having_clause)? );

subquery_from_clause
    : fr='FROM' subselect_identification_variable_declaration (',' subselect_identification_variable_declaration)*
    -> ^(T_SOURCES<FromNode>[$fr] ^(T_SOURCE<SelectionSourceNode> subselect_identification_variable_declaration)*);

subselect_identification_variable_declaration
    : identification_variable_declaration
    | association_path_expression ('AS')? identification_variable
    | collection_member_declaration;

association_path_expression
    : path_expression;

simple_select_clause
    : ('DISTINCT')? simple_select_expression
    -> ^(T_SELECTED_ITEMS ^(T_SELECTED_ITEM<SelectedItemNode>[] ('DISTINCT')? simple_select_expression));

simple_select_expression
    : path_expression
    | aggregate_expression
    | identification_variable;

conditional_expression
    : (conditional_term) ('OR' conditional_term)*;

conditional_term
    : (conditional_factor) ('AND' conditional_factor)*;

conditional_factor
    : ('NOT')? simple_cond_expression -> ^(T_SIMPLE_CONDITION<SimpleConditionNode>[] ('NOT')? simple_cond_expression)
    | '('conditional_expression')';

simple_cond_expression
    : comparison_expression
    | between_expression
    | like_expression
    | in_expression
    | null_comparison_expression
    | empty_collection_comparison_expression
    | collection_member_expression
    | exists_expression
    | date_macro_expression;

date_macro_expression
    : date_between_macro_expression
    | date_before_macro_expression
    | date_after_macro_expression
    | date_equals_macro_expression
    | date_today_macro_expression;

date_between_macro_expression
    : '@BETWEEN' '(' path_expression ',' 'NOW' (('+' | '-') INT_NUMERAL)? ',' 'NOW' (('+' | '-') INT_NUMERAL)? ',' ('YEAR' | 'MONTH' | 'DAY' | 'HOUR' |'MINUTE' | 'SECOND') ')';

date_before_macro_expression
    : '@DATEBEFORE' '(' path_expression ',' (path_expression | input_parameter) ')';

date_after_macro_expression
    : '@DATEAFTER' '(' path_expression ',' (path_expression | input_parameter) ')';

date_equals_macro_expression
    : '@DATEEQUALS' '(' path_expression ',' (path_expression | input_parameter) ')';

date_today_macro_expression
    : '@TODAY' '(' path_expression ')';

between_expression
    : arithmetic_expression ('NOT')? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
    | string_expression ('NOT')? 'BETWEEN' string_expression 'AND' string_expression
    | datetime_expression ('NOT')? 'BETWEEN' datetime_expression 'AND' datetime_expression;

in_expression
    : path_expression ('NOT')? 'IN' in_expression_right_part;

in_expression_right_part
    : '(' in_item (',' in_item)* ')'
    | subquery;

in_item
    : literal
    | input_parameter;

like_expression
    : string_expression ('NOT')? 'LIKE' (pattern_value | input_parameter)('ESCAPE' ESCAPE_CHARACTER)?;

null_comparison_expression
    : (path_expression | input_parameter) 'IS' ('NOT')? 'NULL';

empty_collection_comparison_expression
    : path_expression 'IS' ('NOT')? 'EMPTY';

collection_member_expression
    : entity_expression ('NOT')? 'MEMBER' ('OF')? path_expression;

exists_expression
    : ('NOT')? 'EXISTS' subquery;

all_or_any_expression
    : ( 'ALL' | 'ANY' | 'SOME') subquery;

comparison_expression
    : string_expression comparison_operator (string_expression | all_or_any_expression)
    | boolean_expression ('=' | '<>') (boolean_expression | all_or_any_expression)
    | enum_expression ('='|'<>') (enum_expression | all_or_any_expression)
    | datetime_expression comparison_operator (datetime_expression | all_or_any_expression)
    | entity_expression ('=' | '<>') (entity_expression | all_or_any_expression)
    | arithmetic_expression comparison_operator (arithmetic_expression | all_or_any_expression);

comparison_operator
    : '='
    | '>'
    | '>='
    | '<'
    | '<='
    | '<>';

arithmetic_expression
    : simple_arithmetic_expression
    | subquery;

simple_arithmetic_expression
    : (arithmetic_term) (( '+' | '-' ) arithmetic_term)*;

arithmetic_term
    : (arithmetic_factor) (( '*' | '/' ) arithmetic_factor)*;

arithmetic_factor
    : ( '+' | '-' )? arithmetic_primary;

arithmetic_primary
    : path_expression
    | numeric_literal
    | '('simple_arithmetic_expression')'
    | input_parameter
    | functions_returning_numerics
    | aggregate_expression;

string_expression
    : string_primary | subquery;

string_primary
    : path_expression
    | STRINGLITERAL
    | input_parameter
    | functions_returning_strings
    | aggregate_expression;

datetime_expression
    : datetime_primary
    | subquery;

datetime_primary
    : path_expression
    | input_parameter
    | functions_returning_datetime
    | aggregate_expression;

boolean_expression
    : boolean_primary
    | subquery;

boolean_primary
    : path_expression
    | boolean_literal
    | input_parameter;

enum_expression
    : enum_primary
    | subquery;

enum_primary
    : path_expression
    | enum_literal
    | input_parameter;

entity_expression
    : path_expression
    | simple_entity_expression;

simple_entity_expression
    : identification_variable
    | input_parameter;

functions_returning_numerics
    : 'LENGTH' '('string_primary')'
    | 'LOCATE' '('string_primary',' string_primary(',' simple_arithmetic_expression)?')'
    | 'ABS' '('simple_arithmetic_expression')'
    | 'SQRT' '('simple_arithmetic_expression')'
    | 'MOD' '('simple_arithmetic_expression',' simple_arithmetic_expression')'
    | 'SIZE' '('path_expression')';

functions_returning_datetime
    : 'CURRENT_DATE'
    | 'CURRENT_TIME'
    | 'CURRENT_TIMESTAMP';

functions_returning_strings
    : 'CONCAT' '('string_primary',' string_primary')'
    | 'SUBSTRING' '('string_primary','simple_arithmetic_expression',' simple_arithmetic_expression')'
    | 'TRIM' '('((trim_specification)? (TRIM_CHARACTER)? 'FROM')? string_primary')'
    | 'LOWER' '('string_primary')'
    | 'UPPER' '('string_primary')';

trim_specification
    : 'LEADING'
    | 'TRAILING'
    | 'BOTH';

//my
abstract_schema_name
    : WORD;

//todo fix pattern value if needed
pattern_value
    : WORD;

// my
numeric_literal
    : ('0x')? INT_NUMERAL ;

input_parameter
    : '?' INT_NUMERAL -> ^(T_PARAMETER<ParameterNode>[] '?' INT_NUMERAL )
    | NAMED_PARAMETER -> ^(T_PARAMETER<ParameterNode>[] NAMED_PARAMETER )
    | '${' WORD '}' -> ^(T_PARAMETER<ParameterNode>[] '${' WORD '}');

literal
    : WORD;

constructor_name
    : WORD;

enum_literal
    : WORD;

boolean_literal
    : 'true'
    | 'false';

// my
field
    : WORD | 'GROUP';

identification_variable
    : WORD;

parameter_name
    : WORD ('.'  WORD)*;


// Lexical Rules
//fix trim character
TRIM_CHARACTER
    : '\'.\'';

STRINGLITERAL
    : '\'' (~('\'' | '"') )* '\'' ;

WORD
    : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'$')*;

RUSSIAN_SYMBOLS
    : ('\u0400'..'\u04FF'|'\u0500'..'\u052F' ) {if ( 1 == 1) throw new IllegalArgumentException("Incorrect symbol");};

NAMED_PARAMETER
    : ':'('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'$')* (('.') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'$')+)*;

WS  : (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;

COMMENT
    : '/*' .* '*/' {$channel=HIDDEN;};

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;};

ESCAPE_CHARACTER
    : '\'' (~('\''|'\\') ) '\'';

INT_NUMERAL
    : ('0'..'9')+;
