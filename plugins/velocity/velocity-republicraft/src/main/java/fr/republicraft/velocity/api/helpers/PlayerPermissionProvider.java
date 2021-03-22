package fr.republicraft.velocity.api.helpers;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import lombok.NonNull;

public class PlayerPermissionProvider implements PermissionProvider, PermissionFunction {
//    private final Player player;
//    private final User user;
//    private final QueryOptionsSupplier queryOptionsSupplier;
//
//    public PlayerPermissionProvider(Player player, User user, QueryOptionsSupplier queryOptionsSupplier) {
//        this.player = player;
//        this.user = user;
//        this.queryOptionsSupplier = queryOptionsSupplier;
//    }
//
    @Override
    public @NonNull PermissionFunction createFunction(@NonNull PermissionSubject subject) {
//        Preconditions.checkState(subject == this.player, "createFunction called with different argument");
        return this;
    }

    @Override
    public @NonNull Tristate getPermissionValue(@NonNull String permission) {
//        return this.user.getPermissionData(this.queryOptionsSupplier.getQueryOptions())
//                .checkPermission(permission)
//                .result();
        return Tristate.UNDEFINED;
    }
}
