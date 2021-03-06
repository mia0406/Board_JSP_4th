package com.mia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mia.vo.BoardVO;
import com.mia.vo.UserVO;

public class MvcProcessor {
	
	private static MvcProcessor instance = new MvcProcessor();
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private List<BoardVO> allArticles; // 굳이 전역 변수로 사용하면 좋은 이유가....?
	
	// 지역 변수로 여러번 선언하고 사라지는 것이 효율이 좋은가? 아니면 전역 변수로 한번 선언하고 계속 사용하는 것이 효율이 더 좋은가?
	// DB 쿼리 select문 할 때 *로 전부 다 가져오는 거랑 필요한 요소만 들고오는거랑 시간 차이가 많이 나는가? 
	
	public static MvcProcessor getInstance() {
		return instance;
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException{
	
		String driverName="oracle.jdbc.driver.OracleDriver";
		final String dbUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
		final String dbID = "mia";
		final String dbPassword = "mia";
		
		Connection connection = null;
		Class.forName(driverName);
		connection = DriverManager.getConnection(dbUrl, dbID, dbPassword);
		
		System.out.println("오라클 DB 연결");
		
		return connection;
	}
	
	private Connection getMySqlArticleConnection() throws ClassNotFoundException, SQLException{
		
		String driverName="com.mysql.cj.jdbc.Driver";
		final String mySqlDbUrl = "jdbc:mysql://127.0.0.1:3306/board?characterEncoding=UTF-8&serverTimezone=UTC";
		final String mySqlDbID = "root";
		final String mySqlDbPassword = "root";
		
		Connection connection = null;
		Class.forName(driverName);
		connection = DriverManager.getConnection(mySqlDbUrl, mySqlDbID, mySqlDbPassword);
		
		System.out.println("MySQL DB 연결");
		
		return connection;
	}
	
	public void closeConnection() {
	
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int login(String id, String password) {
		
		int result=0;
		
		try {			
			connection=getConnection();
			pstmt=connection.prepareStatement("SELECT * FROM USERS WHERE id=? and password=?");
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs=pstmt.executeQuery();
			
			if(rs.next() == true) {
				result = 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {			
			closeConnection();
		}
		
		return result;
	}
	
	public void logout() {
		 //session.invalidate();
	}
	
	public void signUp(String id, String password, String name) {

		try {
			
			connection=getConnection();
			pstmt=connection.prepareStatement("INSERT INTO USERS VALUES (?,?,?)");
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.setString(3, name);
			pstmt.executeQuery();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<UserVO> getUserList() {
		
		List<UserVO> usersList = new ArrayList<UserVO>();
		
		try {		
			connection=getConnection();
			
			pstmt=connection.prepareStatement("SELECT * FROM USERS");
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
			
				UserVO user = new UserVO();
				user.setId(rs.getString("ID"));
				user.setName(rs.getString("NAME"));
				
				usersList.add(user);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
			
		return usersList;
	}
	
	public void deleteUser(String id) {
		
		try {
			connection = getConnection();
			pstmt=connection.prepareStatement("DELETE FROM USERS WHERE ID=?");
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {

			if(rs != null) {
				try {
					rs.close();
				} catch(Exception e) {}

			}

			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {}

			}

			if(connection != null) {
				try {
					connection.close();
				} catch(Exception e) {}
			}
		}
	}
	
	public List<BoardVO> getArticles() {

		allArticles = new ArrayList<BoardVO>();

		try {

			connection = this.getConnection();
			pstmt=connection.prepareStatement("SELECT * FROM ARTICLE order by NUM");
			rs = pstmt.executeQuery();

			while(rs.next()) {

				BoardVO boardVO = new BoardVO();		
				boardVO.setNum(rs.getInt("num"));		
				boardVO.setTitle(rs.getString("title"));		
				boardVO.setWriter(rs.getString("writer"));
				boardVO.setHits(rs.getInt("hits"));		
				boardVO.setRecommand(rs.getInt("recommand"));		
				boardVO.setWriteDate(rs.getDate("writeDate"));

				allArticles.add(boardVO);
			}

		} catch(Exception e) {
			e.printStackTrace();
		} finally {

			if(rs != null) {
				try {
					rs.close();
				} catch(Exception e) {}

			}

			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {}

			}

			if(connection != null) {
				try {
					connection.close();
				} catch(Exception e) {}
			}
		}

		return allArticles;
	}
	
	public BoardVO getArticle(int num) {
		
		//BoardVO article = null; // 이 부분을 활용하면 article 부분에 null pointer access 발생해서 기능 제대
		BoardVO article = new BoardVO();
	
		try {

			connection = this.getConnection();
			pstmt=connection.prepareStatement("SELECT * FROM ARTICLE WHERE NUM=?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
			
				//updateHit(num);
				article.setNum(rs.getInt("NUM"));
				article.setTitle(rs.getString("TITLE"));
				article.setContent(rs.getString("CONTENT"));
				article.setWriter(rs.getString("WRITER"));
				article.setWriteDate(rs.getDate("WRITEDATE"));
				article.setHits(rs.getInt("HITS"));
				article.setRecommand(rs.getInt("RECOMMAND"));
			}

		} catch(Exception e) {
			e.printStackTrace();
		} finally {

			if(rs != null) {
				try {
					rs.close();
				} catch(Exception e) {}

			}

			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {}

			}

			if(connection != null) {
				try {
					connection.close();
				} catch(Exception e) {}
			}
		}
		
		return article;
	}
	
	public void updateHit(int num) {
		
		try {
			connection=getConnection();
			pstmt=connection.prepareStatement("UPDATE FROM BOARD SET hits=? WHERE num=?");
			pstmt.setInt(1, getCurrentHits(num)+1);
			pstmt.setInt(2, num);
			pstmt.executeUpdate();			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getCurrentHits(int num) {
		
		int hit=0;		
		try {
			
			pstmt=connection.prepareStatement("SELECT * FROM BOARD WHERE NUM=?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			hit=rs.getInt("hits");
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
			}
		}
		
		return hit;
	}
	
	public void writeArticle(String writer, String title, String content) {
		
		try {			
			connection = getConnection();
			// ARTICLE_sequence.nextval
			pstmt=connection.prepareStatement("INSERT INTO ARTICLE VALUES (?, ?, ?, ?, sysdate, 0, 0)");
			pstmt.setInt(1, 10);
			pstmt.setString(2, title);
			pstmt.setString(3, writer);
			pstmt.setString(4, content);
			int result=pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modifyArticle(int num, String title, String content) {
		
		try {
			connection=getConnection();
			pstmt=connection.prepareStatement("UPDATE FROM BOARD SET TITLE=?, CONTENT=? WHERE NUM=?");
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, num);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void addRecommand(int num) {
		
		try {	
			connection=getConnection();
			pstmt=connection.prepareStatement("UPDATE FROM BOARD SET RECOMMAND=? WHERE NUM=?");
			pstmt.setInt(1, getCurrentRecommands(num)+1);
			pstmt.setInt(2, num);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void cancelRecommand(int num) {
		
		try {
			connection=getConnection();
			pstmt=connection.prepareStatement("UPDATE FROM BOARD SET RECOMMAND=? WHERE NUM=?");
			pstmt.setInt(1, getCurrentRecommands(num)-1);
			pstmt.setInt(2, num);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {		
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getCurrentRecommands(int num) {
		
		int recommand=0;
		
		try {
			
			pstmt=connection.prepareStatement("SELECT RECOMMAND FROM BOARD WHERE NUM=?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			recommand=rs.getInt("RECOMMAND");
			
		}catch(Exception e) {
			
		}finally {	
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return recommand;
	}
	
	public void deleteArticle(int num) {	

		try {
			connection=getConnection();
			pstmt=connection.prepareStatement("DELETE FROM BOARD WHERE NUM=?");
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
