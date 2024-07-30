package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import net.serenitybdd.core.Serenity;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;

import static net.serenitybdd.rest.SerenityRest.rest;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StepDefsPetTypes {
    private final String URI_BASE = "https://petstore3.swagger.io/api/v3";

    @Given("el cliente configura la URI base")
    public void elClienteConfiguraLaURIBase() {
        rest().baseUri(URI_BASE);
    }

    @When("el cliente realiza una peticion GET a {string}")
    public void el_cliente_realiza_una_peticion_get_a(String path) {
        when().get(URI_BASE.concat(path)).andReturn();
    }

    @Then("el servidor debe de responder con un status {int}")
    public void el_servidor_debe_de_responder_con_un_status(Integer statusCode) {
        then().statusCode(statusCode);
    }

    @Then("el cuerpo de la respuesta debe de ser una lista de tipos de mascotas")
    public void el_cuerpo_de_la_respuesta_debe_de_ser_una_lista_de_tipos_de_mascotas() {
        then().body("$", not(empty()));
        then().body("[0].id", notNullValue());
        then().body("[0].name", notNullValue());
    }

    @And("el cuerpo de la respuesta contiene la propiedad id con el valor {int}")
    public void elCuerpoDeLaRespuestaContieneLaPropiedadIdConElValor(Integer value) {
        then().body("id", is(value));
    }

    @And("el cuerpo de la respuesta contiene la propiedad estado con el valor {string}")
    public void elCuerpoDeLaRespuestaContieneLaPropiedadEstadoConElValor(String value) {
        then().body("status", is(value));
    }

    @Then("el cuerpo de la respuesta debe de estar vacio")
    public void el_cuerpo_de_la_respuesta_debe_de_estar_vacio() {
        then().body(is(emptyString()));
    }

    @When("el cliente realiza una peticion DELETE a {string} con id de la orden {int}")
    public void el_cliente_realiza_una_peticion_delete_a_con_id_tipo_de_mascota_eliminado(String path, Integer id) {
        given().pathParam("id", id)
                .delete(URI_BASE.concat(path))
                .andReturn();
    }

    @When("el cliente realiza una peticion GET a {string} con el estado {string}")
    public void elClienteRealizaUnaPeticionGETAConElEstado(String path, String estado) {
        given().param("status", estado)
                .when().get(URI_BASE.concat(path))
                .andReturn();
    }

    @And("el cuerpo de la respuesta debe de ser una lista de mascotas de estado {string}")
    public void elCuerpoDeLaRespuestaDebeDeSerUnaListaDeMascotasDeEstado(String estado) {
        then().body("$", not(empty()));
        then().body("[0].id", notNullValue());
        then().body("[0].name", notNullValue());
        then().body("[0].status", is(estado));
    }

    @Given("el cliente tiene los datos de una nueva orden")
    public void elClienteTieneLosDatosDeUnaNuevaOrden(String docString) {
        Serenity.setSessionVariable("order").to(docString);
    }

    @When("el cliente realiza una peticion POST a {string} con los detalles de la nueva orden")
    public void elClienteRealizaUnaPeticionPOSTAConLosDetallesDeLaNuevaOrden(String path) {
        String orden = Serenity.sessionVariableCalled("order");
        given().contentType(ContentType.JSON)
                .body(orden)
                .post(URI_BASE.concat(path))
                .andReturn();
    }

    @And("el cuerpo de la respuesta debe contener los detalles de la nueva orden registrada")
    public void elCuerpoDeLaRespuestaDebeContenerLosDetallesDeLaNuevaOrdenRegistrada() throws JsonProcessingException {
        String docString = Serenity.sessionVariableCalled("order");
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });

        then().body(notNullValue());
        then().body("id", notNullValue());
        then().body("id", instanceOf(Number.class));
        then().body("status", notNullValue());
        then().body("status", is(jsonMap.get("status")));
    }
}
