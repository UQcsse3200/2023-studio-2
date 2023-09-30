package com.csse3200.game.components.resources;

import com.csse3200.game.components.Component;

public class FissureComponent extends Component {
    public Resource getProduces() {
        return produces;
    }

    Resource produces;

    public FissureComponent(Resource produces) {
        this.produces = produces;
    }
}
