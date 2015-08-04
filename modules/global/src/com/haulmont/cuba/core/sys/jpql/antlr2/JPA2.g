grammar JPA2;

options{
        backtrack=true;//todo eude this is the source of bad performance - try to get rid of it
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
package com.haulmont.cuba.core.sys.jpql.antlr2;

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
package com.haulmont.cuba.core.sys.jpql.antlr2;

}

ql_statement
    : select_statement;

select_statement
     : sl='SELECT' select_clause from_clause (where_clause)? (groupby_clause)? (having_clause)? (orderby_clause)?
     -> ^(T_QUERY<QueryNode>[$sl] (select_clause)? from_clause (where_clause)? (groupby_clause)? (having_clause)? (orderby_clause)?);

update_statement
    : 'UPDATE' update_clause (where_clause)?;
delete_statement
    : 'DELETE' 'FROM' delete_clause (where_clause)?;

from_clause
     : fr='FROM' identification_variable_declaration (',' identification_variable_declaration_or_collection_member_declaration)*
     -> ^(T_SOURCES<FromNode>[$fr] identification_variable_declaration identification_variable_declaration_or_collection_member_declaration*);
identification_variable_declaration_or_collection_member_declaration
     : identification_variable_declaration
     | collection_member_declaration -> ^(T_SOURCE<SelectionSourceNode> collection_member_declaration);

identification_variable_declaration
     : range_variable_declaration joined_clause*
     -> ^(T_SOURCE<SelectionSourceNode> range_variable_declaration joined_clause*);
joined_clause : join | fetch_join;
range_variable_declaration
     : entity_name ('AS')? identification_variable
     -> ^(T_ID_VAR<IdentificationVariableNode>[$identification_variable.text] entity_name);
join
     : join_spec join_association_path_expression ('AS')? identification_variable (join_condition)?
     -> ^(T_JOIN_VAR<JoinVariableNode>[$join_spec.text, $identification_variable.text] join_association_path_expression);
fetch_join
     : join_spec 'FETCH' join_association_path_expression;
join_spec
     : (('LEFT') ('OUTER')? | 'INNER' )? 'JOIN';
join_condition
     : 'ON' conditional_expression;

//Start : here we have simplified joins
join_association_path_expression
     : identification_variable '.' (field'.')* field?
         -> ^(T_SELECTED_FIELD<PathNode>[$identification_variable.text] (field)*)
     |  'TREAT(' identification_variable '.' (field'.')* field? 'AS' subtype ')'
         -> ^(T_SELECTED_FIELD<PathNode>[$identification_variable.text] (field)*);
//End : here we have simplified joins

collection_member_declaration
    : 'IN''(' path_expression ')' ('AS')? identification_variable
    -> ^(T_COLLECTION_MEMBER<CollectionMemberNode>[$identification_variable.text] path_expression );

qualified_identification_variable
    : map_field_identification_variable
    | 'ENTRY('identification_variable')';
map_field_identification_variable : 'KEY('identification_variable')' | 'VALUE('identification_variable')';

//Start : here we have simplified paths
path_expression
    :  identification_variable '.' (field'.')* field?
    -> ^(T_SELECTED_FIELD<PathNode>[$identification_variable.text] (field)*)
    ;
//todo eude treated path
//End : here we have simplified paths

general_identification_variable
    : identification_variable
    | map_field_identification_variable;
update_clause
    : entity_name (('AS')? identification_variable)? 'SET' update_item (',' update_item)*;
update_item
    : (identification_variable'.')(single_valued_embeddable_object_field'.')*single_valued_object_field '=' new_value;
new_value
    : scalar_expression
    | simple_entity_expression
    | 'NULL';
delete_clause
    : entity_name (('AS')? identification_variable)?;
select_clause
    : ('DISTINCT')? select_item (',' select_item)*
    -> ^(T_SELECTED_ITEMS ('DISTINCT')? ^(T_SELECTED_ITEM<SelectedItemNode>[] select_item)*);
select_item
    : select_expression (('AS')? result_variable)?;
select_expression
    : path_expression
    | identification_variable -> ^(T_SELECTED_ENTITY<PathNode>[$identification_variable.text])
    | scalar_expression
    | aggregate_expression
    | 'OBJECT' '('identification_variable')'
    | constructor_expression;
constructor_expression
    : 'NEW' constructor_name '(' constructor_item (',' constructor_item)* ')';
constructor_item
    : path_expression
    | scalar_expression
    | aggregate_expression
    | identification_variable;
aggregate_expression
    : aggregate_expression_function_name '('(DISTINCT)? path_expression')'
    -> ^(T_AGGREGATE_EXPR<AggregateExpressionNode>[] aggregate_expression_function_name '(' ('DISTINCT')? path_expression')')
    | 'COUNT' '('(DISTINCT)? count_argument ')'
    -> ^(T_AGGREGATE_EXPR<AggregateExpressionNode>[] 'COUNT' '(' ('DISTINCT')? count_argument ')')
    | function_invocation;
aggregate_expression_function_name
    : 'AVG' | 'MAX' | 'MIN' |'SUM' | 'COUNT';
count_argument
    : identification_variable | path_expression;
where_clause
    : wh='WHERE' conditional_expression-> ^(T_CONDITION<WhereNode>[$wh] conditional_expression);
groupby_clause
    : 'GROUP' 'BY' groupby_item (',' groupby_item)*
    -> ^(T_GROUP_BY<GroupByNode>[] 'GROUP' 'BY' groupby_item*);
groupby_item
    : path_expression | identification_variable;
having_clause
    : 'HAVING' conditional_expression;
orderby_clause
    : 'ORDER' 'BY' orderby_item (',' orderby_item)*
    -> ^(T_ORDER_BY<OrderByNode>[] 'ORDER' 'BY' orderby_item*);
orderby_item
    : orderby_variable ('ASC')?
     -> ^(T_ORDER_BY_FIELD<OrderByFieldNode>[] orderby_variable ('ASC')?)
    | orderby_variable  'DESC'
    -> ^(T_ORDER_BY_FIELD<OrderByFieldNode>[] orderby_variable 'DESC');
orderby_variable
    : path_expression | general_identification_variable | result_variable;

subquery
    : lp='(' 'SELECT' simple_select_clause subquery_from_clause (where_clause)? (groupby_clause)? (having_clause)? rp=')'
     -> ^(T_QUERY<QueryNode>[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause (where_clause)? (groupby_clause)? (having_clause)? );
subquery_from_clause
    : fr='FROM' subselect_identification_variable_declaration (',' subselect_identification_variable_declaration)*
    -> ^(T_SOURCES<FromNode>[$fr] ^(T_SOURCE<SelectionSourceNode> subselect_identification_variable_declaration)*);

subselect_identification_variable_declaration
    : identification_variable_declaration
    | derived_path_expression 'AS' identification_variable (join)*
    | derived_collection_member_declaration;
derived_path_expression
    : general_derived_path'.'single_valued_object_field
    | general_derived_path'.'collection_valued_field;
general_derived_path
    : simple_derived_path
    | treated_derived_path('.'single_valued_object_field)*
    ;
simple_derived_path
    : superquery_identification_variable //todo eude ('.' single_valued_object_field)*
    ;
treated_derived_path
    : 'TREAT('general_derived_path 'AS' subtype ')';
derived_collection_member_declaration
    : 'IN' superquery_identification_variable'.'(single_valued_object_field '.')*collection_valued_field;

simple_select_clause
    : ('DISTINCT')? simple_select_expression
    -> ^(T_SELECTED_ITEMS ^(T_SELECTED_ITEM<SelectedItemNode>[] ('DISTINCT')? simple_select_expression));
simple_select_expression
    : path_expression
    | scalar_expression
    | aggregate_expression
    | identification_variable;
scalar_expression
    : arithmetic_expression
    | string_expression
    | enum_expression
    | datetime_expression
    | boolean_expression
    | case_expression
    | entity_type_expression;
conditional_expression
    : (conditional_term) ('OR' conditional_term)*;
conditional_term
    : (conditional_factor) ('AND' conditional_factor)*;
conditional_factor
    : ('NOT')? conditional_primary;
conditional_primary
    : simple_cond_expression
    -> ^(T_SIMPLE_CONDITION<SimpleConditionNode>[] simple_cond_expression)
    | '('conditional_expression')';
simple_cond_expression
    : comparison_expression
    | between_expression
    | in_expression
    | like_expression
    | null_comparison_expression
    | empty_collection_comparison_expression
    | collection_member_expression
    | exists_expression
    | date_macro_expression;

//Start: Here we insert our custom macroses
date_macro_expression
    : date_between_macro_expression
    | date_before_macro_expression
    | date_after_macro_expression
    | date_equals_macro_expression
    | date_today_macro_expression;

date_between_macro_expression
    : '@BETWEEN' '(' path_expression ',' 'NOW' (('+' | '-') numeric_literal)? ',' 'NOW' (('+' | '-') numeric_literal)? ',' ('YEAR' | 'MONTH' | 'DAY' | 'HOUR' |'MINUTE' | 'SECOND') ')';

date_before_macro_expression
    : '@DATEBEFORE' '(' path_expression ',' (path_expression | input_parameter) ')';

date_after_macro_expression
    : '@DATEAFTER' '(' path_expression ',' (path_expression | input_parameter) ')';

date_equals_macro_expression
    : '@DATEEQUALS' '(' path_expression ',' (path_expression | input_parameter) ')';

date_today_macro_expression
    : '@TODAY' '(' path_expression ')';
//End: Here we insert our custom macroses

between_expression
    : arithmetic_expression ('NOT')? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
    | string_expression ('NOT')? 'BETWEEN' string_expression 'AND' string_expression
    | datetime_expression ('NOT')? 'BETWEEN' datetime_expression 'AND' datetime_expression;
in_expression
    : (path_expression | type_discriminator) ('NOT')? 'IN'
            ( '(' in_item (',' in_item)* ')'
            | subquery
            | collection_valued_input_parameter );
in_item
    : literal | single_valued_input_parameter;
like_expression
    : string_expression ('NOT')? 'LIKE' (pattern_value | input_parameter)('ESCAPE' escape_character)?;
null_comparison_expression
    : (path_expression | input_parameter) 'IS' ('NOT')? 'NULL';
empty_collection_comparison_expression
    : path_expression 'IS' ('NOT')? 'EMPTY';
collection_member_expression
    : entity_or_value_expression  ('NOT')? 'MEMBER' ('OF')? path_expression;
entity_or_value_expression
    : path_expression
    | simple_entity_or_value_expression;
simple_entity_or_value_expression
    : identification_variable
    | input_parameter
    | literal;
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
    | entity_type_expression ('=' | '<>') entity_type_expression
    | arithmetic_expression comparison_operator (arithmetic_expression | all_or_any_expression);

comparison_operator
    : '='
    | '>'
    | '>='
    | '<'
    | '<='
    | '<>';
arithmetic_expression
    : arithmetic_term
    | arithmetic_term ('+' | '-') arithmetic_term;
arithmetic_term
    : arithmetic_factor
    | arithmetic_factor ( '*' | '/') arithmetic_factor;
arithmetic_factor
    : (( '+' | '-'))? arithmetic_primary;
arithmetic_primary
    : path_expression
    | numeric_literal
    | '('arithmetic_expression')'
    | input_parameter
    | functions_returning_numerics
    | aggregate_expression
    | case_expression
    | function_invocation
    | subquery;
string_expression
    : path_expression
    | string_literal
    | input_parameter
    | functions_returning_strings
    | aggregate_expression
    | case_expression
    | function_invocation
    | subquery;
datetime_expression
    : path_expression
    | input_parameter
    | functions_returning_datetime
    | aggregate_expression
    | case_expression
    | function_invocation
    | date_time_timestamp_literal
    | subquery;
boolean_expression
    : path_expression
    | boolean_literal
    | input_parameter
    | case_expression
    | function_invocation
    | subquery;
enum_expression
    : path_expression
    | enum_literal
    | input_parameter
    | case_expression
    | subquery;
entity_expression
    : path_expression
    | simple_entity_expression;
simple_entity_expression
    : identification_variable
    | input_parameter;
entity_type_expression
    : type_discriminator
    | entity_type_literal
    | input_parameter;
type_discriminator
    : 'TYPE'(general_identification_variable | path_expression | input_parameter);
functions_returning_numerics
    : 'LENGTH('string_expression')'
    | 'LOCATE(' string_expression',' string_expression(','arithmetic_expression)?')'
    | 'ABS('arithmetic_expression')'
    | 'SQRT('arithmetic_expression')'
    | 'MOD('arithmetic_expression',' arithmetic_expression')'
    | 'SIZE('path_expression')'
    | 'INDEX('identification_variable')';
functions_returning_datetime
    : 'CURRENT_DATE'
    | 'CURRENT_TIME'
    | 'CURRENT_TIMESTAMP';
functions_returning_strings
    : 'CONCAT('string_expression',' string_expression (',' string_expression)*')'
    | 'SUBSTRING(' string_expression',' arithmetic_expression (',' arithmetic_expression)?')'
    | 'TRIM('((trim_specification)? (trim_character)? 'FROM')? string_expression ')'
    | 'LOWER('string_expression')'
    | 'UPPER('string_expression')';
trim_specification
    : 'LEADING' | 'TRAILING' | 'BOTH';
function_invocation
    : 'FUNCTION('function_name (',' function_arg)* ')';
function_arg
    : literal
    | path_expression
    | input_parameter
    | scalar_expression;
case_expression
    : general_case_expression
    | simple_case_expression
    | coalesce_expression
    | nullif_expression;
general_case_expression
    : 'CASE' when_clause (when_clause)* 'ELSE' scalar_expression 'END';
when_clause
    : 'WHEN' conditional_expression 'THEN' scalar_expression;
simple_case_expression
    : 'CASE' case_operand simple_when_clause (simple_when_clause)* 'ELSE' scalar_expression 'END';
case_operand
    : path_expression
    | type_discriminator;
simple_when_clause
    : 'WHEN' scalar_expression 'THEN' scalar_expression;
coalesce_expression
    : 'COALESCE('scalar_expression (',' scalar_expression)+')';
nullif_expression
    : 'NULLIF('scalar_expression ',' scalar_expression')';

//Start : Here we insert tail from old grammar
input_parameter
    : '?' numeric_literal -> ^(T_PARAMETER<ParameterNode>[] '?' numeric_literal )
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

escape_character
    : ESCAPE_CHARACTER;
trim_character
    : TRIM_CHARACTER;
string_literal
    : STRING_LITERAL;
numeric_literal
    : ('0x')? INT_NUMERAL ;
single_valued_object_field
    : WORD;
single_valued_embeddable_object_field
    : WORD;
collection_valued_field
    : WORD;
entity_name
    : WORD;
subtype
    : WORD;
entity_type_literal
    : WORD;
function_name
    : WORD;
state_field
    : WORD;
result_variable
    : WORD;
superquery_identification_variable
    : WORD;
date_time_timestamp_literal
    : WORD;
pattern_value
    : string_literal;
collection_valued_input_parameter
    : input_parameter;
single_valued_input_parameter
    : input_parameter;

// Lexical Rules
//fix trim character
TRIM_CHARACTER
    : '\'.\'';

STRING_LITERAL
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
//End : Here we insert tail from old grammar


