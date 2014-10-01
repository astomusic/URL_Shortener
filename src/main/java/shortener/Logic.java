package shortener;

import java.sql.SQLException;

import redis.clients.jedis.Jedis;

public class Logic {
	private Jedis jedis = new Jedis("10.73.45.75", 6379);
	private int key;

	public void initJedis() {
		String StringKey = jedis.get("key");
		if (StringKey == null) {
			jedis.set("key", "0");
			key = 0;
		} else {
			key = Integer.parseInt(StringKey);
		}
		
		System.out.println("init하고 키를 받아온거다 " + key);
	}

	public void jedisSet() {
		jedis.set(key + "", "hello~~~~~~~");
		key++;
		System.out.println("레디스 인덱스 초기화: "+jedis.get("0"));
	}

	public void getConnection() throws IllegalAccessException, ClassNotFoundException, SQLException {	
		initJedis();
	}

	public String getId(String longUrl) throws Exception {
		getConnection();
		String tempQuery = jedis.get(longUrl.trim());
		return tempQuery;
	}

	public String getShort(String serverName, int port, String contextPath, String longUrl) throws Exception {
		getConnection();
		String id = getId(longUrl);// check if URL has been shorten already
		if (id != null) {
			// if id is not null, this link has been shorten already.
			// nothing to do

		} else {
			jedis.set(key+"", longUrl.trim());
			System.out.println("해당 Index에 대해 해당하는 long이 없어서 "+jedis.get(key+"")+ "키로 레디스에 저장");
			id = key+"";
			key++;
			jedis.set("key", key+"");
		}
		System.out.println("여기로 줄여짐! "+ "http://" + serverName + ":" + port + contextPath + "/" + id);
		return "http://" + serverName + ":" + port + contextPath + "/" + id;
	}

	public String getLongUrl(String urlId) throws Exception {
		getConnection();
		if (urlId.startsWith("/")) {
			urlId = urlId.replace("/", "");
		}
		System.out.println(urlId + "로 쇼튼된 URL을 받았으니 긴 URL을 찾으러간다");

		String tempLong = jedis.get(urlId);

		System.out.println("긴걸 찾음 " + tempLong);
		return tempLong;
	}

}