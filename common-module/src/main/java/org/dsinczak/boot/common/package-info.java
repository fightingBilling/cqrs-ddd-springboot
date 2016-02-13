/**
 * Common application logic classes. No not put any business logic components here.
 * If you want to share business logic between modules then create a special module
 * for it (bounded context) because it basically means there is a need for another
 * bounded context. It is very important to hold business logic closed in modules designated
 * for them, this way we gain clear separation and loose coupling.
 */
package org.dsinczak.boot.common;