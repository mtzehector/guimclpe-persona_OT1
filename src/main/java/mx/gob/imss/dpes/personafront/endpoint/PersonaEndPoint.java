/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.Notice;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.service.ConsultarPersonaService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author antonio
 */
@Path("/persona")
@RequestScoped
public class PersonaEndPoint extends BaseGUIEndPoint<Persona, Persona, Persona>{
  
  @Inject
  ConsultarPersonaService consultarPersonaService;
  
  @GET
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Obtener la capacidad de credito del pensionado",
            description = "Obtener la capacidad de credito del pensionado")
  @Override
    public Response load(Persona persona) throws BusinessException{
        log.log(Level.INFO, ">>>>personaFrontOT1 >PersonaEndPoint load Persona={0}",persona);
        
      ServiceDefinition[] steps = { consultarPersonaService };            
      Message<Persona> response = consultarPersonaService.executeSteps(steps, new Message<>(persona) );      
      return toResponse(response);    
    }

}
