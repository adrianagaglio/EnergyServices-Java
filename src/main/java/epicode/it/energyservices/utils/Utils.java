package epicode.it.energyservices.utils;

import epicode.it.energyservices.auth.dto.RegisterRequest;
import epicode.it.energyservices.entities.sys_user.SysUser;

public class Utils {

    public static String getAvatar(RegisterRequest u) {
        return "https://ui-avatars.com/api/?name=" + u.getName() + "+" + u.getSurname();
    }
}
