package it.unipi.chesspuzzles.server;

/**
 * Wrapper for the json response of chess.com's puzzle api
 * <br>Endpoint: https://api.chess.com/pub/puzzle/random
 * <br>Example:
 * <pre>
 * {
 *     "title": "No Air to Breathe",
 *     "comments": "",
 *     "url": "https://www.chess.com/forum/view/daily-puzzles/8132013---no-air-to-breathe",
 *     "publish_time": 1376377200,
 *     "fen": "r2qk3/pp1nb2r/4Qp2/3N3p/8/8/PPP2PPP/2K1RB1R w q - 0 1",
 *     "pgn": "[Date \"????.??.??\"]\r\n[Result \"*\"]\r\n[FEN \"r2qk3/pp1nb2r/4Qp2/3N3p/8/8/PPP2PPP/2K1RB1R w q - 0 1\"]\r\n\r\n1. Qg8+ Nf8 2. Nxf6# \r\n*",
 *     "image": "https://www.chess.com/dynboard?fen=r2qk3/pp1nb2r/4Qp2/3N3p/8/8/PPP2PPP/2K1RB1R%20w%20q%20-%200%201&size=2"
 * }
 * </pre>
 */
@SuppressWarnings({"unused", "JavadocLinkAsPlainText"})
class ChessComApiResponse {
    public String title;
    public String comments;
    public String url;
    public Long publish_time;
    public String fen;
    public String pgn;
    public String image;
}
