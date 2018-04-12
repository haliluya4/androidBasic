package com.jx.demo.network;

import com.jx.framework.network.XServerManager;
import com.jx.demo.utils.DebugMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务实体管理器，保证所有的服务实例最多只有一个，采用懒加载方式创建
 */
public final class ServiceManager {
    private static final Map<Class, Object> sManager = new HashMap<>();

    /**
     * 获取服务实例，会从框架中创建对应接口的实体用于访问网络
     *
     * @param serviceClass 服务接口类
     * @param <T>          接口类型
     * @return 服务接口实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> serviceClass) {
        if (!sManager.containsKey(serviceClass)) {
            synchronized (ServiceManager.class) {
                if (!sManager.containsKey(serviceClass)) {
                    sManager.put(serviceClass,
                            XServerManager.getServerInstance(Api.BASE_URL, HeaderProcessor.getInstance(), DebugMode.isDebugMode()).create(serviceClass));
                }
            }
        }
        return (T) sManager.get(serviceClass);
    }

    /**
     * 清空服务实例，并销毁框架服务管理器
     */
    public static void destroy() {
        sManager.clear();
        XServerManager.destroy();
    }
}
