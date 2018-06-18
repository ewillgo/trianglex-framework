package org.trianglex.common.test;

import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.core.env.ConfigurableEnvironment;

public class JasyptContextLoader extends SpringBootContextLoader {
    @Override
    protected ConfigurableEnvironment getEnvironment() {
        return new StandardEncryptableEnvironment();
    }
}
