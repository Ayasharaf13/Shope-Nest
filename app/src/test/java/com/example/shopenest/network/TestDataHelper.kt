package com.example.shopenest.network

import com.example.shopenest.model.AddressBody
import com.example.shopenest.model.AppliedDiscount
import com.example.shopenest.model.CustomerAddress
import com.example.shopenest.model.CustomerData
import com.example.shopenest.model.DefaultAddress
import com.example.shopenest.model.DraftOrder
import com.example.shopenest.model.ImageBrand
import com.example.shopenest.model.LineItem
import com.example.shopenest.model.Product
import com.example.shopenest.model.TaxLine




var mockCustomerData = CustomerData(
    id = 12345L,
    email = "test@example.com",
    accepts_marketing = false,
    created_at = "2026-09-01T10:00:00Z",
    updated_at = "2026-09-01T10:00:00Z",
    first_name = "Ahmed",
    orders_count = 0,
    state = "disabled",
    total_spent = "0.00",
    last_order_id = null,
    note = null,
    verified_email = true,
    multipass_identifier = null,
    tax_exempt = false,
    phone = "01000000000",
    tags = "",
    last_order_name = null,
    currency = "EGP",
    addresses = emptyList(),
    default_address = DefaultAddress(1L,"",1L,"","","","","",false), // يرجى إضافة قيم DefaultAddress الافتراضية هنا
    accepts_marketing_updated_at = null,
    marketing_opt_in_level = null,
    tax_exemptions = emptyList(),
    admin_graphql_api_id = "gid://shopify/Customer/12345"
)


fun createDummyAddressBody(
    address1: String = "123 Nile Street",
    city: String = "Cairo",
    phone: String = "01000000000",
    zip: String = "11511",
    firstName: String = "Ahmed",
    country: String = "Egypt"
): AddressBody {
    return AddressBody(
        address1 = address1,
        city = city,
        phone = phone,
        zip = zip,
        first_name = firstName,
        country = country
    )
}

fun createDummyCustomerAddress(
    id: Long = 201L,
    customerId: Long = 1001L,
    firstName: String? = "Ahmed",
    address1: String? = "123 Tahrir St",
    city: String? = "Cairo",
    country: String? = "Egypt",
    zip: String? = "11511",
    phone: String? = "01000000000",
    isDefault: Boolean = true
): CustomerAddress {
    return CustomerAddress(
        id = id,
        customer_id = customerId,
        first_name = firstName,
        address1 = address1,
        city = city,
        country = country,
        zip = zip,
        phone = phone,
        isDefault = isDefault
    )
}
fun createDummyDraftOrder(
    idDraftOrder: Long = 1001L,
    email: String = "user@example.com",
    taxesIncluded: Boolean = false,
    currency: String = "EGP",
    createdAt: String = "2026-09-01T10:00:00Z",
    updatedAt: String = "2026-09-01T10:00:00Z",
    taxExempt: Boolean = false,
    completedAt: String? = null,
    name: String = "#DRAFT1001",
    allowDiscountCodes: Boolean? = true,
    b2b: Boolean? = false,
    status: String = "open",
    lineItems: List<LineItem>? = emptyList(),
    shippingAddress: AddressBody = createDummyAddressBody(), // افترضنا وجود Helper Function لـ AddressBody
    billingAddress: AddressBody? = null,
    invoiceUrl: String? = "https://example.com/invoice",
    appliedDiscount: AppliedDiscount? = null,
    orderId: Long? = null,
    taxLines: List<TaxLine>? = emptyList(),
    tags: String? = "draft",
    totalPrice: String = "150.00",
    subtotalPrice: String = "150.00",
    totalTax: String = "0.00",
    customer: CustomerData = mockCustomerData, // باستخدام دالة العميل المساعدة
    defaultAddress: CustomerAddress = createDummyCustomerAddress(),
    adminGraphqlApiId: String? = "gid://shopify/DraftOrder/1001"
): DraftOrder {
    return DraftOrder(
        idDraftOrder = idDraftOrder,
        email = email,
        taxes_included = taxesIncluded,
        currency = currency,
        created_at = createdAt,
        updated_at = updatedAt,
        tax_exempt = taxExempt,
        completed_at = completedAt,
        name = name,
        allow_discount_codes_in_checkout = allowDiscountCodes,
        b2b = b2b,
        status = status,
        line_items = lineItems,
        shipping_address = shippingAddress,
        billing_address = billingAddress,
        invoice_url = invoiceUrl,
        applied_discount = appliedDiscount,
        order_id = orderId,
        tax_lines = taxLines,
        tags = tags,
        total_price = totalPrice,
        subtotal_price = subtotalPrice,
        total_tax = totalTax,
        customer = customer,
       default_address = defaultAddress,
        admin_graphql_api_id = adminGraphqlApiId
    )












}