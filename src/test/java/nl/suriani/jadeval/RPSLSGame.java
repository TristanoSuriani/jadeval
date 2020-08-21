package nl.suriani.jadeval;

import java.util.UUID;

public class RPSLSGame {
	private UUID uuid;
	private Player player1;
	private Player player2;
	private Move movePlayer1;
	private Move movePlayer2;
	private Status status;

	public static int MAXIMUM_SCORE = 3;

	public RPSLSGame(RPSLSGame otherGame) {
		this(otherGame.uuid, otherGame.player1, otherGame.player2, otherGame.movePlayer1, otherGame.movePlayer2, otherGame.status);
	}

	public RPSLSGame(UUID uuid, Player player1, Player player2, Move movePlayer1, Move movePlayer2, Status status) {
		this.uuid = uuid;
		this.player1 = player1;
		this.player2 = player2;
		this.movePlayer1 = movePlayer1;
		this.movePlayer2 = movePlayer2;
		this.status = status;
	}

	public RPSLSGame() {
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public Player getPlayer1() {
		return this.player1;
	}

	public Player getPlayer2() {
		return this.player2;
	}

	public Move getMovePlayer1() {
		return this.movePlayer1;
	}

	public Move getMovePlayer2() {
		return this.movePlayer2;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public void setMovePlayer1(Move movePlayer1) {
		this.movePlayer1 = movePlayer1;
	}

	public void setMovePlayer2(Move movePlayer2) {
		this.movePlayer2 = movePlayer2;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RPSLSGame))
			return false;
		final RPSLSGame other = (RPSLSGame) o;
		if (!other.canEqual((Object) this))
			return false;
		final Object this$uuid = this.uuid;
		final Object other$uuid = other.uuid;
		if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid))
			return false;
		final Object this$player1 = this.player1;
		final Object other$player1 = other.player1;
		if (this$player1 == null ? other$player1 != null : !this$player1.equals(other$player1))
			return false;
		final Object this$player2 = this.player2;
		final Object other$player2 = other.player2;
		if (this$player2 == null ? other$player2 != null : !this$player2.equals(other$player2))
			return false;
		final Object this$movePlayer1 = this.movePlayer1;
		final Object other$movePlayer1 = other.movePlayer1;
		if (this$movePlayer1 == null ? other$movePlayer1 != null : !this$movePlayer1.equals(other$movePlayer1))
			return false;
		final Object this$movePlayer2 = this.movePlayer2;
		final Object other$movePlayer2 = other.movePlayer2;
		if (this$movePlayer2 == null ? other$movePlayer2 != null : !this$movePlayer2.equals(other$movePlayer2))
			return false;
		final Object this$status = this.status;
		final Object other$status = other.status;
		if (this$status == null ? other$status != null : !this$status.equals(other$status))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof RPSLSGame;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $uuid = this.uuid;
		result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
		final Object $player1 = this.player1;
		result = result * PRIME + ($player1 == null ? 43 : $player1.hashCode());
		final Object $player2 = this.player2;
		result = result * PRIME + ($player2 == null ? 43 : $player2.hashCode());
		final Object $movePlayer1 = this.movePlayer1;
		result = result * PRIME + ($movePlayer1 == null ? 43 : $movePlayer1.hashCode());
		final Object $movePlayer2 = this.movePlayer2;
		result = result * PRIME + ($movePlayer2 == null ? 43 : $movePlayer2.hashCode());
		final Object $status = this.status;
		result = result * PRIME + ($status == null ? 43 : $status.hashCode());
		return result;
	}

	public String toString() {
		return "RPSLSGame(uuid=" + this.uuid + ", player1=" + this.player1 + ", player2=" + this.player2 + ", movePlayer1=" + this.movePlayer1 + ", movePlayer2=" + this.movePlayer2 + ", status=" + this.status + ")";
	}

	static class Player {
		private UUID uuid;
		private String username;
		private int score;

		public Player(String username) {
			this.uuid = UUID.randomUUID();
			this.username = username;
			this.score = 0;
		}

		public Player() {
		}

		public void incrementScore() {
			score += 1;
		}

		public UUID getUuid() {
			return this.uuid;
		}

		public String getUsername() {
			return this.username;
		}

		public int getScore() {
			return this.score;
		}

		public void setUuid(UUID uuid) {
			this.uuid = uuid;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public boolean equals(final Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Player))
				return false;
			final Player other = (Player) o;
			if (!other.canEqual((Object) this))
				return false;
			final Object this$uuid = this.uuid;
			final Object other$uuid = other.uuid;
			if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid))
				return false;
			final Object this$username = this.username;
			final Object other$username = other.username;
			if (this$username == null ? other$username != null : !this$username.equals(other$username))
				return false;
			if (this.score != other.score)
				return false;
			return true;
		}

		protected boolean canEqual(final Object other) {
			return other instanceof Player;
		}

		public int hashCode() {
			final int PRIME = 59;
			int result = 1;
			final Object $uuid = this.uuid;
			result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
			final Object $username = this.username;
			result = result * PRIME + ($username == null ? 43 : $username.hashCode());
			result = result * PRIME + this.score;
			return result;
		}

		public String toString() {
			return "RPSLSGame.Player(uuid=" + this.uuid + ", username=" + this.username + ", score=" + this.score + ")";
		}
	}

	enum Move {
		ROCK,
		PAPER,
		SCISSORS,
		LIZARD,
		SPOCK
	}

	enum Status {
		NEW,
		WAITING_FOR_PLAYER2,
		NEW_ROUND,
		WAITING_FOR_MOVE_PLAYER1,
		WAITING_FOR_MOVE_PLAYER2,
		READY_FOR_EVALUATION,
		PLAYER1_WON,
		PLAYER2_WON,
		CANCELLED_BY_PLAYER1,
		CANCELLED_BY_PLAYER2
	}
}
