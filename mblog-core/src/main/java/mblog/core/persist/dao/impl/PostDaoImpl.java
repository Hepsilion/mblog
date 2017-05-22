/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import mblog.base.lang.Consts;
import mblog.base.lang.EnumPrivacy;
import mblog.core.data.Post;
import mblog.core.persist.dao.PostDao;
import mblog.core.persist.entity.PostPO;
import mblog.core.persist.utils.BeanMapUtils;
import mtons.modules.annotation.Repository;
import mtons.modules.lang.Const;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
@Repository(entity = PostPO.class)
public class PostDaoImpl extends BaseRepositoryImpl<PostPO> implements PostDao {
	private static final long serialVersionUID = -8144066308316359853L;
	
	@Override
	public List<PostPO> paging(Paging paging, int group, String ord) {
		PagingQuery<PostPO> q = pagingQuery(paging);
		
		if (group > Const.ZERO) {
			q.add(Restrictions.eq("group", group));
		}
		
		if (Consts.order.HOTTEST.equals(ord)) {
			q.desc("views");
		}

		q.add(Restrictions.eq("privacy", EnumPrivacy.OPEN.getIndex()));
		q.desc("featured");
		q.desc("created");
		return q.list();
	}

	@Override
	public List<PostPO> paging4Admin(Paging paging, long id, String title, int group) {
		PagingQuery<PostPO> q = pagingQuery(paging);

		if (group > Const.ZERO) {
			q.add(Restrictions.eq("group", group));
		}

		if (StringUtils.isNotBlank(title)) {
			q.add(Restrictions.like("title", title, MatchMode.ANYWHERE));
		}

		if (id > Const.ZERO) {
			q.add(Restrictions.eq("id", id));
		}
		q.desc("featured");
		q.desc("created");
		return q.list();
	}

	@Override
	public List<PostPO> pagingByAuthorId(Paging paging, long userId, EnumPrivacy privacy) {
		PagingQuery<PostPO> q = pagingQuery(paging);
		if (userId > 0) {
			q.add(Restrictions.eq("authorId", userId));
		}

		if (privacy != null && privacy != EnumPrivacy.ALL) {
			q.add(Restrictions.eq("privacy", privacy.getIndex()));
		}
		q.desc("created");
		return q.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PostPO> findLatests(int maxResults, long ignoreUserId) {
		Criteria c = createCriteria();
		
		c.add(Restrictions.neOrIsNotNull("title", ""));
		c.add(Restrictions.eq("privacy", EnumPrivacy.OPEN.getIndex()));

		if (ignoreUserId > 0) {
			c.add(Restrictions.ne("authorId", ignoreUserId));
		}
		c.addOrder(Order.desc("created"));
		c.setMaxResults(maxResults);
		return c.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PostPO> findHots(int maxResults, long ignoreUserId) {
		Criteria c = createCriteria();
		
		c.add(Restrictions.neOrIsNotNull("title", ""));
		c.add(Restrictions.eq("privacy", EnumPrivacy.OPEN.getIndex()));
//		if (ignoreUserId > 0) {
//			q.add(Restrictions.ne("author.id", ignoreUserId));
//		}
		c.addOrder(Order.desc("views"));
		c.setMaxResults(maxResults);
		return c.list();
	}

	@Override
	public List<PostPO> findByIds(Collection<Long> ids) {
		return find(Restrictions.in("id", ids));
	}

	@Override
	public int maxFeatured() {
		Criteria c = createCriteria();
		c.setProjection(Projections.max("featured"));
		Number num = (Number) c.uniqueResult();
		if (num != null) {
			return num.intValue();
		}
		return 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Post> search(Paging paging, String q) throws Exception {

		FullTextSession fullTextSession = Search.getFullTextSession(super.session());
		SearchFactory sf = fullTextSession.getSearchFactory();
		QueryBuilder qb = sf.buildQueryBuilder().forEntity(PostPO.class).get();

		org.apache.lucene.search.Query luceneQuery  = null;

		MustJunction term = qb.bool().must(qb.keyword().onFields("title","summary","tags").matching(q).createQuery());

		term.must(qb.keyword()
				.onField("privacy")
				.matching(EnumPrivacy.OPEN.getIndex()).createQuery());

		luceneQuery = term.createQuery();

		FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery);
		query.setFirstResult(paging.getFirstResult());
		query.setMaxResults(paging.getMaxResults());

	    StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
	    SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
        QueryScorer queryScorer = new QueryScorer(luceneQuery);
        Highlighter highlighter = new Highlighter(formatter, queryScorer);
        
		List<PostPO> list = query.list();
	    List<Post> rets = new ArrayList<>(list.size());

	    for (PostPO po : list) {
			Post m = BeanMapUtils.copy(po, 0);

			// 处理高亮
			String title = highlighter.getBestFragment(standardAnalyzer, "title", m.getTitle());
			String summary = highlighter.getBestFragment(standardAnalyzer, "summary", m.getSummary());

			if (StringUtils.isNotEmpty(title)) {
				m.setTitle(title);
			}
			if (StringUtils.isNotEmpty(summary)) {
				m.setSummary(summary);
			}
			rets.add(m);
		}

		paging.setTotalCount(query.getResultSize());
		return rets;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PostPO> searchByTag(Paging paigng, String tag) {
		FullTextSession fullTextSession = Search.getFullTextSession(super.session());
		SearchFactory sf = fullTextSession.getSearchFactory();
		QueryBuilder qb = sf.buildQueryBuilder().forEntity(PostPO.class).get();

		org.apache.lucene.search.Query luceneQuery  = null;

		MustJunction term = qb.bool().must(qb.phrase().onField("tags").sentence(tag).createQuery());

		term.must(qb.keyword()
				.onField("privacy")
				.matching(EnumPrivacy.OPEN.getIndex()).createQuery());

		luceneQuery = term.createQuery();

	    FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery);
	    query.setFirstResult(paigng.getFirstResult());
	    query.setMaxResults(paigng.getMaxResults());

		Sort sort = new Sort(new SortField("id", SortField.Type.LONG, true));
		query.setSort(sort);
		
		paigng.setTotalCount(query.getResultSize());
		
		return query.list();
	}

	@Override
	public void resetIndexs() {
		FullTextSession fullTextSession = Search.getFullTextSession(super.session());
		//异步
		fullTextSession.createIndexer(PostPO.class).start();
	}

}
