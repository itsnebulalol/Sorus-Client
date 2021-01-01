

package org.sorus.oneseventen;

import org.sorus.client.version.*;

import java.util.ArrayList;
import java.util.List;


public class Version implements IVersion {

    private final List<Object> datas = new ArrayList<>();

    public Version() {
        this.datas.add(new Renderer(this));
        this.datas.add(new GLHelper());
        this.datas.add(new Screen());
        this.datas.add(new Game());
        this.datas.add(new Input(this));
    }

    @Override
    public <T> T getData(Class<T> clazz) {
        for(Object object : this.datas) {
            if(clazz.isAssignableFrom(object.getClass())) {
                return (T) object;
            }
        }
        return null;
    }
}
