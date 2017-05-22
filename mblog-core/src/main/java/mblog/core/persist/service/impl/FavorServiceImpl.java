package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import mblog.core.data.Favor;
import mblog.core.data.Post;
import mblog.core.persist.dao.FavorDao;
import mblog.core.persist.entity.FavorPO;
import mblog.core.persist.service.FavorService;
import mblog.core.persist.service.PostService;
import mblog.core.persist.utils.BeanMapUtils;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu on 2015/8/31.
 */
@Service
public class FavorServiceImpl implements FavorService {
    @Autowired
    private FavorDao favorDao;
    @Autowired
    private PostService postService;

    @Override
    @Transactional
    public void add(long userId, long postId) {
        FavorPO po = favorDao.find(userId, postId);

        Assert.isNull(po, "已经喜欢过此文章了");

        // 如果没有喜欢过, 则添加记录
        po = new FavorPO();
        po.setOwnId(userId);
        po.setPostId(postId);
        po.setCreated(new Date());

        favorDao.save(po);
    }

    @Override
    @Transactional
    public void delete(long userId, long postId) {
        FavorPO po = favorDao.find(userId, postId);
        Assert.notNull(po, "还没有喜欢过此文章");
        favorDao.delete(po);
    }

    @Override
    @Transactional(readOnly = true)
    public void pagingByOwnId(Paging paging, long ownId) {
        List<FavorPO> list = favorDao.paingByOwnId(paging, ownId);

        List<Favor> rets = new ArrayList<>();
        Set<Long> postIds = new HashSet<>();
        for (FavorPO po : list) {
            rets.add(BeanMapUtils.copy(po));
            postIds.add(po.getPostId());
        }

        if (postIds.size() > 0) {
            Map<Long, Post> posts = postService.findMultileMapByIds(postIds);

            for (Favor t : rets) {
                Post p = posts.get(t.getPostId());
                t.setPost(p);
            }
        }
        paging.setResults(rets);
    }
}
