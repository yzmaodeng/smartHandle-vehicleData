package com.xinghe.xbx.service.util.sequence;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sequenceService")
public class SequenceService  {

	private static final Logger logger = LoggerFactory.getLogger(SequenceService.class);

	public static final int SeqIdCapacity = 100;

	public static final String ID_INDEX = "id_index";

	public static final String ID_LAST = "id_last";

	public static final String SPAN_ID = "span";
	
	@Resource(name="sequenceDao")
	private SequenceDao sequenceDao;
	
	protected static Map<String, ConcurrentLinkedQueue<Long>> queueMap;

	static {
		queueMap = new HashMap<String, ConcurrentLinkedQueue<Long>>();
	}
	/**
	 * 得到和某个sequence对应的 queue
	 * @param sequence
	 * @return
	 */
	public ConcurrentLinkedQueue<Long> getQueue(String seqName) {
		ConcurrentLinkedQueue<Long> queue = queueMap.get(seqName);
		if (null == queue) {
			queue = new ConcurrentLinkedQueue<Long>();
			queueMap.put(seqName, queue);
		}
		return queue;
	}
	/**
	 * 暴露给外面使用的接口，用于得到某个Sequence当前可用的id
	 * @param sequence
	 * @return
	 */
	public synchronized Long nextValue(String seqName) {
		if (StringUtils.isBlank(seqName)) {
			logger.info("Attention: Sequence's name is null");
			return null;
		}
		ConcurrentLinkedQueue<Long> queue = getQueue(seqName);
		int size = queue.size();
		if (size <= SeqIdCapacity) {
			Map<String, Long> ids = null;
			try{
				ids = getSequence(seqName);
			}catch(Exception e){
				e.getStackTrace();
				logger.error("Error: Cant' get sequence : " + seqName + " from DB ");
			}
			
			if (null != ids && ids.size() > 0) {
				Long span = ids.get(SPAN_ID);
				Random random = new Random();
				Long minId = ids.get(ID_INDEX);
				Long maxId = ids.get(ID_LAST);

				if (logger.isInfoEnabled()) {
					logger.info("min id = " + minId + " max id = " + maxId + " and span = " + span );
				}
				for (long i = minId.longValue();;) {
					i += (random.nextInt(span.intValue()) + 1);
					if (i > maxId.longValue()) {
						break;
					}
					queue.add(new Long(i));
				}
			} else {
				logger.error("Error: Cant' get sequence : " + seqName + " from DB null == ids || ids.size() == 0");
			}
		}
		Long returnId = null;
		returnId = queue.poll();
		if (null == returnId) {
			logger.error("Error : Failed to get sequence :" + seqName + " from queue null == returnId");
		}
		return returnId;
	}
	
	@Transactional
	public Map<String, Long> getSequence(String seqname){
		Map<String,Long> map = new HashMap<String,Long>();
		try {
			map = sequenceDao.getSequence(seqname);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			sequenceDao.updateSequence(seqname);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return map;
	}

}
