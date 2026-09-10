package com.example.shopenest.model

fun createDummyProduct(
    id: Long = 1L,
    title: String = "Default Dummy Product",


    ): Product {
    return Product(
        id = id,
        title = title,


        // يمكنك إكمال بقية حقول الـ Product Data Class الخاصة بك بنفس الطريقة
    )
}

fun createDummyDraftOrderUpdateBody(
    id: Long = 9001L,
    appliedDiscount: AppliedDiscount = AppliedDiscount()
): DraftOrderUpdateBody {
    return DraftOrderUpdateBody(
        id = id,
        appliedDiscount = appliedDiscount
    )
}

fun createDummyDraftOrderHeader(
    // 1. البرامترات الأساسية التي تتغير غالباً في الاختبارات
    draftOrderId: Long = 101L,
    customerId: Long = 1L,
    email: Any? = "user@example.com",
    totalPrice: String? = "150.00",
    status: String? = "open",

    // 2. إمكانية تخصيص الكائنات المتداخلة عند الحاجة
    shippingAddress: AddressBody = createDummyAddressBody(),
    billingAddress: AddressBody? = null,
    appliedDiscount: AppliedDiscount? = null,
    defaultAddress: CustomerAddress? = null
): DraftOrderHeaderEntity {

    return DraftOrderHeaderEntity(
        draftOrderId = draftOrderId,
        email = email,
        taxes_included = true,
        currency = "EGP",
        created_at = "2026-09-08T10:00:00Z",
        updated_at = "2026-09-08T10:00:00Z",
        tax_exempt = false,
        completed_at = null,
        name = "#D1001",
        allow_discount_codes_in_checkout = true,
        status = status,
        api_client_id = null,
        customerId = customerId,
        totalPrice = totalPrice,
        subtotalPrice = "150.00",
        createdAt = "2026-09-08T10:00:00Z",
        updatedAt = "2026-09-08T10:00:00Z",
        shipping_address = shippingAddress,
        billing_address = billingAddress,
        invoice_url = "https://example.com/invoice.pdf",
        applied_discount = appliedDiscount,
        order_id = 5001L,
        tags = "draft, pending",
        total_price = totalPrice,
        subtotal_price = "150.00",
        total_tax = "0.00",
        default_address = defaultAddress
    )
}

// دالة مساعدة لإنشاء AddressBody افتراضي لكي لا تحتاج لكتابته يدوياً كل مرة
fun createDummyAddressBody(
    address1: String = "123 Cairo St",
    city: String = "Cairo",
    country: String = "Egypt"
): AddressBody {
    return AddressBody(
        address1 = address1,
        city = city,
        country = country
        // أضف باقي حقول AddressBody بنفس الطريقة
    )
}
fun createDummyInventoryLevel(
    inventoryItemId: Long = 1001L,
    locationId: Long = 5001L,
    available: Int = 25,
    updatedAt: String = "2026-09-09T10:00:00Z",
    adminGraphqlApiId: String = "gid://shopify/InventoryLevel/1001?location_id=5001"
): InventoryLevel {
    return InventoryLevel(
        inventory_item_id = inventoryItemId,
        location_id = locationId,
        available = available,
        updated_at = updatedAt,
        admin_graphql_api_id = adminGraphqlApiId
    )
}


fun createDummyDiscountCode(
    id: Long = 101L,
    priceRuleId: Long = 501L,
    code: String = "SUMMER2026",
    usageCount: Int = 0,
    createdAt: String = "2026-09-09T10:00:00Z",
    updatedAt: String = "2026-09-09T10:00:00Z"
): DiscountCode {
    return DiscountCode(
        id = id,
        price_rule_id = priceRuleId,
        code = code,
        usage_count = usageCount,
        created_at = createdAt,
        updated_at = updatedAt
    )
}


fun createDummyCustomerData(
    id: Long = 1L,
    email: String = "user@example.com",
    firstName: String? = "Ali",
    phone: String? = "+201000000000",
    addresses: List<CustomerAddress> = emptyList(),
    defaultAddress: DefaultAddress = createDummyDefaultAddress()
): CustomerData {
    return CustomerData(
        id = id,
        email = email,
        accepts_marketing = true,
        created_at = "2026-09-09T10:00:00Z",
        updated_at = "2026-09-09T10:00:00Z",
        first_name = firstName,
        orders_count = 2,
        state = "disabled",
        total_spent = "500.00",
        last_order_id = 1001L,
        note = null,
        verified_email = true,
        multipass_identifier = null,
        tax_exempt = false,
        phone = phone,
        tags = "VIP",
        last_order_name = "#1001",
        currency = "EGP",
        addresses = addresses,
        default_address = defaultAddress,
        accepts_marketing_updated_at = "2026-09-09T10:00:00Z",
        marketing_opt_in_level = "single_opt_in",
        tax_exemptions = emptyList(),
        admin_graphql_api_id = "gid://shopify/Customer/1"
    )
}

fun createDummyDefaultAddress(
    id: Long = 10L,
    customerId: Long = 1L,
    firstName: String = "Ali",
    phone: String = "+201000000000",
    address1: String = "123 Cairo St",
    city: String = "Cairo",
    zip: String = "11511", // تصحيح: إضافة نوع البيانات String
    country: String = "Egypt",
    default: Boolean = true
): DefaultAddress {
    return DefaultAddress(
        id = id,
        first_name = firstName,
        customer_id = customerId,
        phone = phone,
        address1 = address1,
        city = city,
        zip = zip,
        country = country,
        default = default
    )
}


fun createDummyCustomCollection(
    id: Long ,
    title: String ,
    handle: String = "nike",
    image: ImageBrand? = createDummyImageBrand(),
    updatedAt: String = "2026-09-09T10:00:00Z",
    publishedAt: String = "2026-09-09T10:00:00Z",
    sortOrder: String = "best-selling",
    bodyHtml: String? = "<p>Nike products</p>"
): CustomCollection {
    return CustomCollection(
        id = id,
        handle = handle,
        title = title,
        updatedAt = updatedAt,
        bodyHtml = bodyHtml,
        publishedAt = publishedAt,
        sortOrder = sortOrder,
        image = image
    )
}



fun createDummyImageBrand(
    src: String = "https://example.com/brand_logo.png",
    width: Long = 300L,
    height: Long = 300L,
    createdAt: String = "2026-09-09T10:00:00Z"
): ImageBrand {
    return ImageBrand(
        createdAt = createdAt,
        width = width,
        height = height,
        src = src
    )








}