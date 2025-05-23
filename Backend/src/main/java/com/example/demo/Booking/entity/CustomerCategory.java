package com.example.demo.Booking.entity;

// 고객 분류
// 각 분류별로 할인율이나 가격을 적용할수 있음
public enum CustomerCategory {
	ADULT("성인"),
	YOUTH("청소년"),
	SENIOR("노인"),
	DISABLED("장애인");

	private final String description;
	CustomerCategory(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	// 문자열로부터 해당 enum 상수를 찾음, 없으면 기본값(ADULT)을 반환합니다.
	public static CustomerCategory fromString(String category) {
		for (CustomerCategory b : CustomerCategory.values()) {
			if (b.name().equalsIgnoreCase(category)) {
				return b;
			}
		}
		return ADULT; // 기본값
	}
}
