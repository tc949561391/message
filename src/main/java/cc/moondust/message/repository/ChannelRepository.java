package cc.moondust.message.repository;

import io.netty.channel.Channel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j0 on 2016/8/2.
 */
@Repository
public class ChannelRepository {
    public static Map<String, Channel> channelMaps = new HashMap<>();

    public void saveChannel(String key, Channel channel) {
        channelMaps.put(key, channel);
    }

    public Channel getChannel(String key) {
        Channel channel = channelMaps.get(key);
        return channel;
    }

    public void removeChannel(String key){
        channelMaps.remove(key);
    }

    public void removeChannelObj(Channel channel){
        channelMaps.remove(channel);
    }


}
