package hello.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import io.reactivex.Single;

import javax.validation.constraints.Size;

@Controller("/echo")
public class EchoController {

    @Post(value = "/sync", consumes = MediaType.TEXT_PLAIN)
    public String echoSync(@Size(max = 1024) @Body String text) {
        return text;
    }

    @Post(value = "/async", consumes = MediaType.TEXT_PLAIN)
    public Single<MutableHttpResponse<String>> echoFlow(@Body Flowable<String> text) {
        return text.collect(StringBuffer::new, StringBuffer::append)
                   .map(buffer ->
                           HttpResponse.ok(buffer.toString())
                   );
    }
}