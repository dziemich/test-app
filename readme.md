# Scala Basic Calc


## Task
Implement a calculator that evaluates given mathematical
expression in parallel.
### Requirements:
 - The code MUST be version controlled
 - The microservice MUST be implemented in Scala, Akka
 - The microservice MUST be implemented in Functional Style
 - The microservice MUST handle the input via HTTP and
respond with the evaluated result or validation error.

### Supported operations:
- \+ addition
- \- subtraction
- \/ division
- \* multiplication
- \( left parenthesis
- \) right parenthesis

```
Example:
$ curl -H " Content-Type: application/json " \ -X POST \
-d ' {"expression":"(1-1)*2+3*(1-3+4)+10/2"} ' \
http: //localhost:5555/evaluate

{" result ": 11}
```

## Comentary
Validator and Calculator have been implemented as two seperate entities. The example is very simple.
This was done having in mind that the Validator might need to verify something against an external service (as this is usually the case in real world applications) and this system allows to do that in a comfortable fashion.

## Running
Run
```
sbt "runMain dziemich.calculator.Server"
```