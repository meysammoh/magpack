package ir.mjm.filtering;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

/**
 * Created by Serap on 6/27/14.
 */
@ApplicationPath("/webapi/*")
public class MyApplication extends ResourceConfig {

  public MyApplication() {
    super(
        new ResourceConfig()
            .packages("ir.mjm.restpkg")
            .register(MultiPartFeature.class).register(RolesAllowedDynamicFeature.class));
  }
}
