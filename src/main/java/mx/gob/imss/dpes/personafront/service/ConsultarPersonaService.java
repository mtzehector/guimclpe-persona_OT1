/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.personafront.model.PersonaRequest;
import mx.gob.imss.dpes.personafront.restclient.PersonaEnrolamientoClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author antonio
 */
@Provider
public class ConsultarPersonaService extends ServiceDefinition<Persona, Persona> {

    @Inject
    @RestClient
    private PersonaEnrolamientoClient personaEnrolamientoClient;

    @Override
    public Message<Persona> execute(Message<Persona> request) throws BusinessException {
        // Se busca en el sistema de enrolamiento y despues de nuestra base
        PersonaRequest peticion = new PersonaRequest();
        peticion.setCurp(request.getPayload().getCurp());
        Response load = null;
        try {
            load = personaEnrolamientoClient.load(peticion);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            
            if (e.getMessage().contains("Unknown error, status code 400") || e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "ServiciosDigitales");
                ExceptionUtils.throwServiceException("ServiciosDigitales");
            }
            if (e.getMessage().contains("Unknown error, status code 406")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Persona no encontrada CURP={0}", peticion.getCurp());
                throw new PersonaNoRegistradaException();
            }
            throw new UnknowException();
        }

        if (load.getStatus() == 200) {
            Persona persona = load.readEntity(Persona.class);
            return new Message<>(persona);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }

}
