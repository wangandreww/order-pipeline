const LABELS = {
  PLACED: 'Placed',
  CONFIRMED: 'Confirmed',
  REJECTED: 'Rejected',
}

function StatusBadge({ status }) {
  // key={status} makes React replace the element when the status changes, which replays the pop-in animation
  return (
    <span key={status} className={`badge badge-${status.toLowerCase()}`}>
      {status === 'PLACED' && <span className="spinner" aria-hidden="true" />}
      {LABELS[status] ?? status}
    </span>
  )
}

export default StatusBadge
