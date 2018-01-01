package com.trychen.logitow.forge.binding;

import java.util.UUID;

public class BlockBinding {
    private UUID device;
    private int blockID;
    private BindingType type;

    enum BindingType {
        COMMAND;
    }
}
