package mblog.core.persist.service;

import mtons.modules.pojos.Paging;

/**
 * @author langhsu on 2015/8/31.
 */
public interface FavorService {
    /**
     *
     * @param userId
     * @param postId
     * @return
     */
    void add(long userId, long postId);
    void delete(long userId, long postId);

    /**
     * 分页查询用户的喜欢记录
     * @param paging
     * @param ownId
     */
    void pagingByOwnId(Paging paging, long ownId);
}
