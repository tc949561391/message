package cc.moondust.message.annotation;

import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by Tristan on 16/7/17.
 */

@Target(ElementType.TYPE)
@Controller
public @interface MessageSocket{
}
