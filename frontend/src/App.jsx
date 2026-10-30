import { getOrders, getProducts } from './api'
import OrderForm from './components/OrderForm'
import OrdersTable from './components/OrdersTable'
import StockPanel from './components/StockPanel'
import { usePolling } from './usePolling'

// How often to re-fetch. Orders change status asynchronously, so the page has to keep asking.
const POLL_MS = 1500

function App() {
  const orders = usePolling(getOrders, POLL_MS)
  const products = usePolling(getProducts, POLL_MS)

  const error = orders.error ?? products.error

  return (
    <div className="page">
      <header className="header">
        <div>
          <h1>Order Pipeline</h1>
          <p className="subtitle">Spring Boot services coordinating through Kafka</p>
        </div>
        <span className={`live ${error ? 'down' : ''}`}>{error ? 'Disconnected' : 'Live'}</span>
      </header>

      {error && (
        <p className="banner">
          Can't reach a service ({error}). Is everything running? Retrying…
        </p>
      )}

      <div className="layout">
        <aside className="side">
          <section className="card">
            <h2>Place an order</h2>
            {/* Refresh right away instead of waiting for the next poll */}
            <OrderForm products={products.data ?? []} onPlaced={orders.refresh} />
          </section>
          <section className="card">
            <h2>Inventory</h2>
            {products.data ? <StockPanel products={products.data} /> : <p className="empty">Loading…</p>}
          </section>
        </aside>

        <section className="card">
          <h2>Orders</h2>
          {orders.data ? <OrdersTable orders={orders.data} /> : <p className="empty">Loading…</p>}
        </section>
      </div>
    </div>
  )
}

export default App
