package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.CategoryService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.testdata.TestObjects;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration tests for {@link CategoryServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class CategoryServiceImplIT {

    @Autowired
    private CategoryService categoryService;
    
    private CategoryVO controlCategoryVO;
    
    @BeforeEach
    public void setup() throws IOException {
        controlCategoryVO = TestObjects.CATEGORY_VO_1;
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void testGetOne() throws ServiceException {

        // when
        CategoryVO result = categoryService.getOne(controlCategoryVO.getId());

        // then
        assertThat(result.getTitle(), equalTo(controlCategoryVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void testGetAll() {

        // when
        List<CategoryVO> result = categoryService.getAll();

        // then
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.get(0).getTitle(), equalTo(controlCategoryVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void testCreateOne() throws ServiceException {

        // given
        CategoryVO createdCategory = TestObjects.CATEGORY_VO_NEW;

        // when
        Long result = categoryService.createOne(createdCategory);

        // then
        assertThat(categoryService.getOne(result).getTitle(), equalTo(createdCategory.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedCategoryTitle = "Category updated title";
        Long id = 1L;
        CategoryVO updateVO = CategoryVO.getBuilder()
                .withId(id)
                .withTitle(updatedCategoryTitle)
                .build();

        // when
        CategoryVO result = categoryService.updateOne(id, updateVO);

        // then
        CategoryVO updatedCategoryVO = categoryService.getOne(id);
        assertThat(result.getTitle(), equalTo(updatedCategoryVO.getTitle()));
        assertThat(result.getTitle(), equalTo(updatedCategoryTitle));
    }
}
