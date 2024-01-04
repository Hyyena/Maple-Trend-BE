rootProject.name = "Maple-Trend-BE"

// 공통 모듈 계층
include("common")

// 내부 모듈 계층
include("nexon-open-api-core")

// 도메인 모듈 계층
include("maple-stamp-domain-mariadb")

// 애플리케이션 모듈 계층
include("app-maple-stamp-api")