import StatusBadge from './StatusBadge'

function OrdersTable({ orders }) {
  if (orders.length === 0) {
    return <p className="empty">No orders yet. Place one to watch it move through the pipeline.</p>
  }

  return (
    <div className="table-wrap">
      <table className="orders">
        <thead>
          <tr>
            <th>Order</th>
            <th>Product</th>
            <th className="num">Qty</th>
            <th>Status</th>
            <th>Details</th>
            <th>Placed</th>
          </tr>
        </thead>
        <tbody>
          {/* key tells React which row is which between renders, so rows update in place */}
          {orders.map((order) => (
            <tr key={order.id}>
              <td className="mono">#{order.id}</td>
              <td className="mono">{order.productId}</td>
              <td className="num">{order.quantity}</td>
              <td>
                <StatusBadge status={order.status} />
              </td>
              <td className="reason">{order.rejectionReason ?? ''}</td>
              <td className="muted">{new Date(order.createdAt).toLocaleTimeString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default OrdersTable
