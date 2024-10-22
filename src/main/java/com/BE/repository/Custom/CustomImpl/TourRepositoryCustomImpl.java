package com.BE.repository.Custom.CustomImpl;

import com.BE.model.entity.TourEntity;
import com.BE.model.request.FindTourRequestDTO;
import com.BE.repository.Custom.TourRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public class TourRepositoryCustomImpl implements TourRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public static void filterCondition(FindTourRequestDTO findTourRequestDTO, StringBuilder where){
        String nameFarm = findTourRequestDTO.getNameFarm();
        if(nameFarm != null && !nameFarm.trim().isEmpty()){
            where.append(" AND f.farm_name LIKE '%"+ nameFarm +"%' ");
        }
        String koiType = findTourRequestDTO.getKoiType();
        if(koiType != null && !koiType.trim().isEmpty()){
            where.append(" AND k.koi_name LIKE '%"+ koiType +"%' ");
        }
        long price  = findTourRequestDTO.getPrice();
        if(price != 0){
            where.append(" AND t.tour_price < "+ price +" ");
        }
        LocalDate dayStart = findTourRequestDTO.getStartTime();
        if(dayStart != null){
            where.append(" AND t.tour_start = '"+ dayStart +"' ");
        }
        LocalDate dayEnd = findTourRequestDTO.getEndTime();
        if(dayEnd != null){
            where.append(" AND t.tour_end = '"+ dayEnd +"' ");
        }
    }
    @Override
    public Page<TourEntity> findAllTour(FindTourRequestDTO findTourRequestDTO, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT t.* FROM tours t " +
                " INNER JOIN farm_tour ft on ft.id = t.id " +
                " INNER JOIN farms f on f.id = ft.id " +
                " INNER JOIN farm_koi fk on fk.id = f.id " +
                " INNER JOIN koifish k on f.id = fk.id ");

        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        filterCondition(findTourRequestDTO, where);
        where.append(" GROUP BY t.id ");

        // Thêm ORDER BY
        sql.append(where).append(" ORDER BY t.id ASC "); // Thay đổi theo yêu cầu sắp xếp

        // Sử dụng LIMIT và OFFSET cho phân trang
        sql.append(" LIMIT ").append(pageable.getPageSize()).append(" OFFSET ").append(pageable.getOffset());

        Query query = entityManager.createNativeQuery(sql.toString(), TourEntity.class);
        List<TourEntity> results = query.getResultList();

        // Tính tổng số bản ghi
        String countQuery = "SELECT COUNT(*) FROM tours t " +
                " INNER JOIN farm_tour ft on ft.id = t.id " +
                " INNER JOIN farms f on f.id = ft.id " +
                " INNER JOIN farm_koi fk on fk.id = f.id " +
                " INNER JOIN koifish k on f.id = fk.id " +
                " WHERE 1=1 " +
                " AND f.farm_name LIKE '%" + findTourRequestDTO.getNameFarm() + "%'"; // Thêm điều kiện lọc nếu cần
        Query countQ = entityManager.createNativeQuery(countQuery);
        System.out.println(sql);
        long totalCount = ((Number) countQ.getSingleResult()).longValue();

        return new PageImpl<>(results, pageable, totalCount);
    }


}
