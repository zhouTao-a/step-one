package com.cloud.redis.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 * @Author zhouT
 *
 */
@Component
public class RedisUtils {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	private final StringRedisTemplate stringRedisTemplate;

	public RedisUtils(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/**
	 * 指定缓存失效时间
	 * 
	 * @param key  键
	 * @param time 时间(秒)
	 */
	public void expire(String key, long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
		} catch (Exception ignored) {

		}
	}

	/**
	 * 根据key 获取过期时间
	 * 
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				redisTemplate.delete(key[0]);
			} else {
				redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
			}
		}
	}

	// ============================String=============================
	/**
	 * 普通缓存获取
	 * 
	 * @param key 键
	 * @return 值
	 */
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 普通缓存放入并设置时间
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 * 
	 * @param key 键
	 * @param delta  要增加几(大于0)
	 * @return
	 */
	public long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 * 
	 * @param key 键
	 * @param delta  要减少几(小于0)
	 * @return
	 */
	public long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

	// ================================Map=================================
	/**
	 * HashGet
	 * 
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取hashKey对应的所有键值
	 * 
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<Object, Object> hmget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 * 
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 * 
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hmset(String key, Map<String, Object> map, long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, Object value, long time) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 * 
	 * @param key  键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * 
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * 
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public double hincr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * 
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public double hdecr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================
	/**
	 * 根据key获取Set中的所有值
	 * 
	 * @param key 键
	 * @return
	 */
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 * 
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 * 
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key, long time, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 * 
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	// ===============================list=================================

	/**
	 * 获取list缓存的内容
	 * 
	 * @param key   键
	 * @return
	 */
	public List<Object> lGet(String key) {
		return lGet(key, 0, -1);
	}
	/**
	 * 获取list缓存的内容
	 *
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return
	 */
	public List<Object> lGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 * 
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, Object value, long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<Object> value, long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * 
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lUpdateIndex(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 *
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key, long count, Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 *
	 * 这个命令会将键 key 的值在偏移量 offset 的位置上设置为 1
	 * 未设置过的情况会返回false
	 * 预设大小可以指定偏移量offset的大小
	 * @param key 键值
	 * @param offset 偏移量
	 * @param value 值
	 * @return 布尔
	 */
	public Boolean setBit(String key, long offset, boolean value) {
        return stringRedisTemplate.opsForValue().setBit(key, offset, value);
	}

	/**
	 * 返回键 key 在偏移量 offset 的位置上的位值
	 * @param key 键值
	 * @param offset 偏移量
	 * @return 布尔
	 */
	public Boolean getBit(String key, long offset) {
		return stringRedisTemplate.opsForValue().getBit(key, offset);
	}

	/**
	 * 统计被设置为 1 的位的数量
	 * @param key 键值
	 * @return Long
	 */
	public Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
	}

	/**
	 * 查找第一个被设置为指定值的位的位置
	 * @param key 键值
	 * @return Long
	 */
	public Long bitTop(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitPos(key.getBytes(), true));
	}



	// 设置单个字段
	public void setHashField(String key, String field, Object value) {
		redisTemplate.opsForHash().put(key, field, value);
	}

	// 获取单个字段
	public String getHashField(String key, String field) {
		return (String) redisTemplate.opsForHash().get(key, field);
	}

	// 批量设置字段
	public void setMultipleHashFields(String key, Map<String, Object> fields) {
		redisTemplate.opsForHash().putAll(key, fields);
	}

	// 批量获取字段
	public List<Object> getMultipleHashFields(String key, List<Object> fields) {
		return redisTemplate.opsForHash().multiGet(key, fields);
	}

	// 获取所有字段和值
	public Map<Object, Object> getAllHashFields(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	// 删除字段
	public void deleteHashFields(String key, Object... fields) {
		redisTemplate.opsForHash().delete(key, fields);
	}

	// 检查字段是否存在
	public boolean hashFieldExists(String key, String field) {
		return redisTemplate.opsForHash().hasKey(key, field);
	}

	// 获取字段数量
	public long getHashLength(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	// 增加字段值
	public long incrementHashField(String key, String field, long increment) {
		return redisTemplate.opsForHash().increment(key, field, increment);
	}

	// 增加字段值（浮点数）
	public double incrementHashFieldByFloat(String key, String field, double increment) {
		return redisTemplate.opsForHash().increment(key, field, increment);
	}

	// 获取所有字段
	public Set<Object> getHashFields(String key) {
		return redisTemplate.opsForHash().keys(key);
	}

	// 获取所有值
	public List<Object> getHashValues(String key) {
		return redisTemplate.opsForHash().values(key);
	}

	// 添加成员到集合
	public void addMembers(String key, Object... members) {
		redisTemplate.opsForSet().add(key, members);
	}

	// 获取集合中的所有成员
	public Set<Object> getMembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	// 移除集合中的成员
	public void removeMembers(String key, Object... members) {
		redisTemplate.opsForSet().remove(key, members);
	}

	// 判断成员是否在集合中
	public boolean isMember(String key, Object member) {
		return redisTemplate.opsForSet().isMember(key, member);
	}

	// 获取集合的成员数量
	public long getMemberCount(String key) {
		return redisTemplate.opsForSet().size(key);
	}

	// 随机移除并返回一个成员
	public Object popMember(String key) {
		return redisTemplate.opsForSet().pop(key);
	}

	// 获取集合的交集
	public Set<Object> getIntersect(String key, String otherKey) {
		return redisTemplate.opsForSet().intersect(key, otherKey);
	}

	// 获取集合的并集
	public Set<Object> getUnion(String key, String otherKey) {
		return redisTemplate.opsForSet().union(key, otherKey);
	}

	// 获取集合的差集
	public Set<Object> getDifference(String key, String otherKey) {
		return redisTemplate.opsForSet().difference(key, otherKey);
	}




	// 添加成员到有序集合
	public void addZMember(String key, double score, Object member) {
		redisTemplate.opsForZSet().add(key, member, score);
	}

	// 获取有序集合中的所有成员
	public Set<Object> getZMembers(String key) {
		return redisTemplate.opsForZSet().range(key, 0, -1);
	}

	// 获取有序集合中的所有成员及其分数
	public Set<Object> getZMembersWithScores(String key) {
		return Collections.singleton(redisTemplate.opsForZSet().rangeWithScores(key, 0, -1));
	}

	// 获取有序集合中指定成员的分数
	public Double getZScore(String key, Object member) {
		return redisTemplate.opsForZSet().score(key, member);
	}

	// 获取有序集合中成员的排名
	public Long getZRank(String key, Object member) {
		return redisTemplate.opsForZSet().rank(key, member);
	}

	// 获取有序集合中成员的逆序排名
	public Long getZReverseRank(String key, Object member) {
		return redisTemplate.opsForZSet().reverseRank(key, member);
	}

	// 移除有序集合中的一个或多个成员
	public void removeZMembers(String key, Object... members) {
		redisTemplate.opsForZSet().remove(key, members);
	}

	// 为有序集合中的成员的分数加上增量
	public Double incrementZScore(String key, Object member, double increment) {
		return redisTemplate.opsForZSet().incrementScore(key, member, increment);
	}

	// 获取有序集合的成员数量
	public Long getZMemberCount(String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}

	// 获取有序集合中指定分数区间的成员数量
	public Long getZCountByScoreRange(String key, double min, double max) {
		return redisTemplate.opsForZSet().count(key, min, max);
	}

	// 设置指定 key 的值
	public void setValue(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	// 获取指定 key 的值
	public Object getValue(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	// 删除指定 key
	public void deleteValue(String key) {
		redisTemplate.delete(key);
	}

	// 将指定 key 的值加 1
	public Long incrementValue(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

	// 将指定 key 的值减 1
	public Long decrementValue(String key) {
		return redisTemplate.opsForValue().decrement(key);
	}

	// 将指定 key 的值追加到当前值的末尾
	public Integer appendValue(String key, String value) {
		return redisTemplate.opsForValue().append(key, value);
	}

	// 获取指定 key 的值的子字符串
	public String getRange(String key, long start, long end) {
		return redisTemplate.opsForValue().get(key, start, end);
	}

	// 将指定 key 的值的子字符串设置为给定值
	public void setRange(String key, Object value, long offset) {
		redisTemplate.opsForValue().set(key, value, offset);
	}

	// 同时设置多个 key-value 对
	public void setMultipleValues(Map<String, Object> keyValueMap) {
		redisTemplate.opsForValue().multiSet(keyValueMap);
	}

	// 获取所有给定 key 的值
	public List<Object> getMultipleValues(Collection<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}
}
