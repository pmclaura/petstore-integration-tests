Feature: Gestionar tienda de mascotas

    @listarMascotasPorEstado
    Scenario: Listar todas las mascotas por estado
      Given el cliente configura la URI base
      When el cliente realiza una peticion GET a "/pet/findByStatus" con el estado "available"
      Then el servidor debe de responder con un status 200
      And el cuerpo de la respuesta debe de ser una lista de mascotas de estado "available"

    @listarMascotasPorEstadoInvalido
    Scenario: Listar todas las mascotas por estado invalido
      Given el cliente configura la URI base
      When el cliente realiza una peticion GET a "/pet/findByStatus" con el estado "error"
      Then el servidor debe de responder con un status 400

    @agregarNuevaOrden
    Scenario: Registrar nueva orden
      Given el cliente tiene los datos de una nueva orden
       """
         {
          "id": 20,
          "petId": 198772,
          "quantity": 7,
          "shipDate": "2024-07-30T23:17:45.181Z",
          "status": "approved",
          "complete": true
          }
       """
      When el cliente realiza una peticion POST a "/store/order" con los detalles de la nueva orden
      Then el servidor debe de responder con un status 200
      And el cuerpo de la respuesta debe contener los detalles de la nueva orden registrada

    @listarUnaOrdenPorId
    Scenario Outline: Listar una orden por id
      Given el cliente configura la URI base
      When el cliente realiza una peticion GET a <uri>
      Then el servidor debe de responder con un status <statusCode>
      And el cuerpo de la respuesta contiene la propiedad id con el valor <value>
      And el cuerpo de la respuesta contiene la propiedad estado con el valor <value1>
      Examples:
        | uri               | statusCode | value | value1  |
        | "/store/order/10"  | 200        | 10    | "approved" |
        | "/store/order/19"  | 200        | 19    | "approved"  |

    @eliminarUnaOrdenId
    Scenario: Eliminar una orden por id
      Given el cliente configura la URI base
      When el cliente realiza una peticion DELETE a "/store/order/{id}" con id de la orden 20
      Then el servidor debe de responder con un status 200
      And el cuerpo de la respuesta debe de estar vacio