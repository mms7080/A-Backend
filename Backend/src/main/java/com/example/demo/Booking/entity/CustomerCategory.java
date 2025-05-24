package com.example.demo.Booking.entity;

// 고객 유형
public enum CustomerCategory {
        ADULT("성인"),
        YOUTH("청소년"),
        SENIOR("경로"),
        DISABLED("장애인");

        private final String description;

        CustomerCategory(String description){
            this.description = description;
        }

        // 해당 고객 유형 설명 반환
        public String getDescription(){
            return description;
        }

        public static CustomerCategory fromString(String categoryText){
            if(categoryText != null){
                for(CustomerCategory category : CustomerCategory.values()){
                    if(category.name().equalsIgnoreCase(categoryText) || category.getDescription().equalsIgnoreCase(categoryText)){
                        return category;
                    }
                }
            }
            // 기본값 반환 또는 예외 발생 등의 처리
        // 여기서는 null이나 빈 문자열이 들어올 경우, 혹은 매칭되는 값이 없을 경우 ADULT를 기본으로 반환하도록 함.
        // 실제 서비스에서는 좀 더 엄격한 검증이나 명확한 예외 처리가 필요할 수 있음.
            return ADULT;
        }
}
